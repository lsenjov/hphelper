(ns hphelper.server.live.api
  (:require [hphelper.server.shared.sql :as sql]
            [taoensso.timbre :as log]
            [clojure.spec :as s]
            [hphelper.server.shared.spec :as ss]
            [clojure.data.json :as json]
            [hphelper.server.live.control :refer [get-game] :as lcon]
            [hphelper.server.shared.helpers :as help]
            )
  (:gen-class)
)

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
       :missions (filter (fn [mission]
                           (some #{(mission :ss_id)}
                                 (map :ss_id
                                      (or (:programGroup p) (get p "Program Group")))))
                         (g :societies))
       }
      (:login errors)
      )
    )
  )

(defn get-player-character-sheet
  "Returns a player's character sheet"
  [^String gUid ^String uUid]
  (if-let [p (-> (get-game gUid) :hps (get uUid))]
    {:character p}
    (:login errors)
    ))

(defn admin-debug
  "Gets the current gamemap, requires admin login"
  [^String gUid ^String uUid]
  (log/trace "admin-debug. gUid:" gUid "uUid:" uUid)
  (if-let [g (is-admin-get-game gUid uUid)]
      (json/write-str g)
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

(defn get-player-directives
  "Gets the directives of a player's service group"
  [^String gUid ^String uUid]
  (let [g (get-game gUid)
        log1 (log/info "Got game")
        p (-> g :hps (get uUid))
        log2 (log/info "Got player:" p)
        sgids (set (map :sg_id (filter #(= (:name p) (:owner %)) (:serviceGroups g))))
        log3 (log/info "Got sgids:" (pr-str sgids))
        ret (->> (:directives g)
                 (filter #(sgids (:sg_id %)))
                 )
        log4 (log/info "ret:" (pr-str ret))
        ]
    (if p
      {:directives ret}
      (:login errors)
      )
    )
  )

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

    ;; Players
    :hps (get-player-character-sheet gUid uUid)
    :missions (get-player-society-missions gUid uUid)
    :directives (get-player-directives gUid uUid)
    :serviceGroups (get-minions gUid uUid)
    (do (log/trace "Could not do index in get-index:" ind) {})
    ))

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
              {:status "ok" :updated t}
              (map (partial get-index gUid uUid) retKeys)))
    (:invalidGame errors)
    ))

