(ns hphelper.server.live.api
  (:require
            [taoensso.timbre :as log]
            [clojure.spec :as s]
            [hphelper.server.shared.spec :as ss]

            [hphelper.server.shared.sql :as sql]
            [hphelper.server.shared.saveload :as sl]
            [clojure.data.json :as json]
            [hphelper.server.live.control :refer [get-game] :as lcon]
            [hphelper.server.shared.helpers :as help]
            )
  (:gen-class)
)

;; Helpers
(def errors
  "A map of precompiled error messages for an invalid game or user id login"
  {:login {:status "error" :message "Invalid game or user id"}
   :invalidGame {:status "error" :message "Invalid game"}
   }
  )
(defn- is-admin-get-game
  "Returns the gamemap if the user is an admin, else nil"
  [^String gUid ^String uUid]
  (if-let [g (get-game gUid)]
    (if (= uUid (:adminPass g))
      g
      nil)
    nil))

;; Publics
(defn get-indicies
  "Gets the indicies of a game"
  [^String gUid]
  (log/trace "get-indicies:" gUid)
  (if-let [gi (:indicies (get-game gUid))]
    {:status "ok" :indicies gi}
    (:invalidGame errors)
    ))

(defn get-cbay
  "Gets the cbay items of a game"
  [^String gUid]
  (log/trace "get-cbay:" gUid)
  (if-let [gc (:cbay (get-game gUid))]
    {:status "ok" :cbay gc}
    (:invalidGame errors)
    ))
(defn get-keywords
  "Gets the cbay items of a game"
  [^String gUid]
  (log/trace "get-keywords:" gUid)
  (if-let [gc (:keywords (get-game gUid))]
    {:status "ok" :keywords gc}
    (:invalidGame errors)
    ))
(defn get-news
  "Gets the news items of a game"
  [^String gUid]
  (log/trace "get-news:" gUid)
  (if-let [gc (:news (get-game gUid))]
    {:status "ok" :news gc}
    (:invalidGame errors)
    ))
(defn get-current-access
  "Gets the current access totals of a game"
  [^String gUid]
  (log/trace "get-current-access:" gUid)
  (if-let [ga (:access (get-game gUid))]
    {:status "ok" :access ga}
    (:invalidGame errors)
    ))
(defn get-current-zone
  "Returns the current zone name"
  [^String gUid]
  (if-let [gz (-> gUid get-game :zone)]
    {:status "ok" :zone gz}
    (:invalidGame errors)
    )
  )
(defn get-investments
  "Gets map of current investments in a game"
  [^String gUid]
  (if-let [g (get-game gUid)]
    (let [vests (lcon/get-investments)]
      {:investments
       (->> g
            ;; Get a list of all the hp names
            :hps
            vals
            (map :name)
            ;; Get all the investment maps, some vals may be nil
            (map (fn [n] [n (get vests n)]))
            ;; Merge into a single map again
            (apply merge {})
            )
       }
      )
    (:invalidGame errors)
    )
  )

;; Players
(defn get-minions-single
  "Takes a record of a single service group, stripping out minions if it isn't the
  authorised player and not bought, returns the edited service group
  g ::liveScenario
  group ::serviceGroupRecord"
  [g ^String uUid group]
  (log/trace "get-minions-single. uUid:" uUid "group:" (:sg_abbr group))
  ;; The owner of a service group will be their HP name
  (if (or
        ; Is this player the owner of the group?
        (and (contains? group :owner)
             (= (:owner group) (-> g :hps (get uUid) :name)))
        ; Is this player the admin?
        (= (:adminPass g) uUid)
        )
    ; This is the owner, give them all the information
    (do (log/trace "Not stripping skills, user is owner") group)
    ; This is NOT the owner, strip out skills of minions
    (update-in group [:minions] (comp (fn [mlist]
                                        (log/trace "Stripping :mskills and :minion_cost")
                                        (map (comp #(dissoc % :mskills)
                                                   #(dissoc % :minion_cost))
                                             mlist)
                                        )
                                      (fn [mlist]
                                        (log/trace "Removing non-bought minions")
                                        (filter #(:bought? %)
                                                mlist)
                                        )
                                      )
               )
    )
  )
(defn get-minions
  "Gets the minions of a game, stripping out the skills of the minions which the player isn't authorised to see"
  ([^String gUid]
   (get-minions gUid ""))
  ([^String gUid ^String uUid]
   (log/trace "get-minions:" gUid uUid)
   (if-let [g (get-game gUid)]
     {:status "okay" :serviceGroups (:serviceGroups (update-in g [:serviceGroups] #(map (partial get-minions-single g uUid) %)))}
     (:invalidGame errors)
     )
   )
  )
(defn get-player-society-missions
  "Gets a list of the secret society missions of a player"
  [^String gUid ^String uUid]
  (let [g (get-game gUid)
        p (-> g :hps (get uUid))]
    (if p
      {:status "ok"
       :missions (filter
                   (let [pg (or (:programGroup p) (get p "Program Group"))
                         ssids-unfil (concat (map :ss_parent pg) (map :ss_id pg))
                         ssids (set (filter identity ssids-unfil))]
                     (fn [mission]
                       (ssids (:ss_id mission))))
                   (g :societies))
       }
      (if (= uUid (:adminPass g))
        ;; If we're an admin, return all the society missions
        {:missions (:societies g)}
        (:login errors)
        )
      )
    )
  )
(defn get-player-character-sheet
  "Returns a player's character sheet"
  [^String gUid ^String uUid]
  (let [g (get-game gUid)
        p (-> g :hps (get uUid))]
    (cond
      ;; Player
      p
      {:character (pr-str p)}
      ;; Admin
      (= uUid (:adminPass g))
      {:hps (pr-str (:hps g))}
      ;; Wrong game
      (not g)
      (:invalidGame errors)
      ;; Other error
      :error
      (:login errors)
    )))
(defn player-buy-minion
  [^String gUid ^String uUid ^String sgid ^String minionid]
  (log/trace "player-buy-minion:" gUid uUid sgid minionid)
  (let [g (get-game gUid)
        p (-> g :hps (get uUid) :name)
        sg (help/parse-int sgid)
        m (help/parse-int minionid)
        ]
    (cond
      ;; Game doesn't exist
      (not g)
      (:invalidGame errors)
      ;; Invalid player
      (not p)
      (:login errors)
      ;; couldn't parse sgid or minionid
      (not (and sg m))
      {:status "error" :message "Could not parse argument"}
      ;; TODO ensure they player owns the minion
      ;; All seems good
      :all-good
      (if (lcon/purchase-minion gUid p sg m)
        {:status "okay"}
        {:status "error" :message "Purchasing minion failed"}
        )
      )
    )
  )
(defn player-send-access
  "Sends the amount of access to another player"
  [^String gUid ^String uUid ^String player-to ^String amount]
  (log/trace "player-send-access:" gUid uUid player-to amount)
  (let [g (get-game gUid)
        p (-> g :hps (get uUid) :name)]
    (cond
      ;; Game doesn't exist
      (not g)
      (:invalidGame errors)
      ;; Invalid player
      (not p)
      (:login errors)
      ;; All seems good
      :all-good
      (if (lcon/send-access gUid p player-to amount)
        {:status "okay"}
        {:status "error" :message "Sending access failed"}
        )
      )
    )
  )
(defn get-player-directives
  "Gets the directives of a player's service group"
  [^String gUid ^String uUid]
  (let [g (get-game gUid)]
    (if-let [p (-> g :hps (get uUid))]
      (let
        [sgids (set (map :sg_id (filter #(= (:name p) (:owner %)) (:serviceGroups g))))
         ret (->> (:directives g)
                  (filter #(sgids (:sg_id %)))
                  )
         ]
        {:directives ret}
        )
      ;; Not a player, maybe an admin?
      (if (= (:adminPass g) uUid)
        ;; We're an admin
        {:directives (:directives g)}
        ;; Wrong login
        (:login errors)
        )
      )
    )
  )
(defn player-trade-investment
  "Buys or sells an amount of cash on the market"
  [^String gUid ^String uUid ^String group ^String amount]
  (log/trace "player-send-access:" gUid uUid group amount)
  (let [g (get-game gUid)
        p (-> g :hps (get uUid) :name)
        zone (:zone g)
        ]
    (cond
      ;; Game doesn't exist
      (not g)
      (:invalidGame errors)
      ;; Invalid player
      (not p)
      (:login errors)
      ;; Invalid group
      (not (s/valid? ::ss/sg_abbr group))
      {:status "error" :message "Invalid Group"}
      ;; All seems good
      :all-good
      (if (lcon/player-trade-investment
            gUid p zone group
            (try (Integer/parseInt amount)
                 (catch NumberFormatException e
                   (log/trace "Failed to parse:" amount)
                   0
                   )
                 )
            )
        {:status "ok"}
        {:status "error" :message "Invalid argument"}
        )
      )
    )
  )

;; Admin commands
(defn admin-debug
  "Gets the current gamemap, requires admin login"
  [^String gUid ^String uUid]
  (log/trace "admin-debug. gUid:" gUid "uUid:" uUid)
  (if-let [g (is-admin-get-game gUid uUid)]
      (pr-str g)
      (:login errors)
    ))
(defn admin-validate-spec
  "Gets the game, validates the gamemap. requires admin login"
  [^String gUid ^String uUid]
  (log/trace "admin-validate-spec. gUid:" gUid "uUid:" uUid)
  (if-let [g (is-admin-get-game gUid uUid)]
    (s/explain-str ::ss/liveScenario g)
    (:login errors)
    ))
(defn admin-modify-index
  "Modifies an index by the required amount"
  [^String gUid ^String uUid ^String ind ^String amount]
  (log/trace "admin-modify-index." gUid uUid ind amount)
  (if-let [g (is-admin-get-game gUid uUid)]
    (if (lcon/modify-index gUid
                           (keyword ind)
                           (try (Integer/parseInt amount)
                                (catch Exception e
                                  (log/debug "Could not parse:" amount "Defaulting to 0")
                                  0)))
      (json/write-str {:status "ok"})
      (json/write-str {:status "error" :message "modify-index failed"}))
    (:login errors)
    ))
(defn admin-modify-access
  "Modifies an index by the required amount"
  [^String gUid ^String uUid ^String player ^String amount]
  (log/trace "admin-modify-index." gUid uUid player amount)
  (if-let [g (is-admin-get-game gUid uUid)]
    (if (lcon/modify-access gUid
                            player
                            (try (Integer/parseInt amount)
                                 (catch Exception e
                                   (log/debug "Could not parse:" amount "Defaulting to 0")
                                   0)))
      (json/write-str {:status "ok"})
      (json/write-str {:status "error" :message "modify-index failed"}))
    (:login errors)
    ))
(defn admin-modify-public-standing
  "Modifies public standing of a player"
  [^String gUid ^String uUid ^String player ^String amount]
  (log/trace "admin-modify-index." gUid uUid player amount)
  (if-let [g (is-admin-get-game gUid uUid)]
    (if (lcon/modify-public-standing
          gUid
          player
          (try (Integer/parseInt amount)
               (catch Exception e
                 (log/debug "Could not parse:" amount "Defaulting to 0")
                 0)))
      (json/write-str {:status "ok"})
      (json/write-str {:status "error" :message "modify-public-standing failed"}))
    (:login errors)
    ))
(defn admin-set-sg-owner
  "Changes the owner of a service group"
  [^String gUid ^String uUid ^String serviceGroup ^String newOwner]
  (log/trace "admin-set-sg-owner." gUid uUid serviceGroup newOwner)
  (if-let [g (is-admin-get-game gUid uUid)]
    (if-let [n (help/is-hp-name? g newOwner)]
      (if (lcon/set-sg-owner gUid serviceGroup newOwner)
        {:status "okay"}
        {:status "error" :message "Unknown failure."}
        )
      {:status "error" :message "Invalid user name"}
      )
    (:login errors)
    )
  )
(defn- sync-single-char
  "Saves a single character from a gamemap to the database"
  [g p-name]
  (log/trace "sync-single-char:" p-name)
  (if-let [c (->> g :hps vals (some #(if (= p-name (:name %)) % nil)))]
    (try
      (-> c
          (assoc :accessRemaining (-> g :access first (get p-name)))
          sl/update-char
          )
      ;; If a character is missing something, it may not save. This is here to prevent it stopping the other characters
      (catch Exception e (log/info "Failed to save character" p-name "Exception:" e)))
    )
  )
(defn sync-chars
  "Save all the characters in a game to the database"
  [^String gUid ^String uUid]
  (log/trace "sync-chars:" gUid uUid)
  (if-let [g (is-admin-get-game gUid uUid)]
    (do
      (doall (map sync-single-char (repeat g) (->> g :hps vals (map :name))))
      {:status "ok"}
      )
    (:invalidGame errors)
    )
  )
(defn admin-lock-zone
  "Sets the lock or unlock status of a zone"
  [^String gUid ^String uUid ^String status]
  (log/trace "admin-lock-zone." gUid uUid status)
  (if-let [g (is-admin-get-game gUid uUid)]
    (lcon/set-lock (:zone g) (if (or (= status true) (= status "true")) true false))))


;; Aggregete get-updated
(defn- get-index
  "Gets the data from an index, returns a map. Returns an empty map if no result"
  [^String gUid ^String uUid ind]
  (log/trace "get-index. gUid:" gUid "ind:" ind)
  (case ind
    ;; Publics
    :news (get-news gUid)
    :cbay (get-cbay gUid)
    :indicies (get-indicies gUid)
    :access (get-current-access gUid)
    :zone (get-current-zone gUid)
    :keywords (get-keywords gUid)
    :investments (get-investments gUid)

    ;; Players
    :hps (get-player-character-sheet gUid uUid)
    :missions (get-player-society-missions gUid uUid)
    :directives (get-player-directives gUid uUid)
    :serviceGroups (get-minions gUid uUid)
    (do (log/trace "Could not do index in get-index:" ind) {})
    )
  )
(defn get-updated-public
  "Gets all the updated items in a game"
  [^String gUid ^Integer t]
  (log/trace "get-updated-public. gUid:" gUid "time:" t)
  (if-let [g (get-game gUid)]
    (let [retKeys (keys (filter (fn [[k lastUpdate]] (< t lastUpdate)) (:updated g)))]
      (log/trace "get-updated-player. retKeys:" retKeys)
      (reduce merge
              {:status "ok" :updated (apply max (vals (:updated g)))}
              (map (partial get-index gUid "") retKeys)
              )
      )
    (:invalidGame errors)
    ))
(defn get-updated-player
  "Gets all the updated items for a player"
  [^String gUid ^String uUid ^Integer t]
  (log/trace "get-updated-player. gUid:" gUid "uUid:" uUid "time:" t)
  (if-let [g (get-game gUid)]
    (let [retKeys (keys (filter (fn [[k lastUpdate]] (< t lastUpdate)) (:updated g)))]
      (reduce merge
              {:status "ok"
               ;; Returns the max value of the keys
               :updated (reduce max 0 (vals (:updated g)))}
              (map (partial get-index gUid uUid) retKeys)))
    (:invalidGame errors)
    ))
(defn get-updated-admin
  "Gets the updated items for an admin"
  [^String gUid ^String uUid ^Integer t]
  (log/trace "get-updated-admin. gUid:" gUid "uUid:" uUid "time:" t)
  (if-let [g (get-game gUid)]
    (let [retKeys (keys (filter (fn [[k lastUpdate]] (< t lastUpdate)) (:updated g)))]
      (->> retKeys
           (map (partial get-index gUid uUid))
           (reduce merge {})
           (merge {:status "ok"
                   ;; Returns the max value of the keys
                   :updated (reduce max 0 (vals (:updated g)))}
                  )
           )
      )
    (:invalidGame errors)
    )
  )
