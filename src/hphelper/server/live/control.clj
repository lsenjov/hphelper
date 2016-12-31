(ns hphelper.server.live.control
  (:require [hphelper.server.shared.sql :as sql]
            [hphelper.server.shared.saveload :as sl]
            [hphelper.server.shared.unique :as uni]
            [hphelper.server.shared.indicies :as indicies]
            [taoensso.timbre :as log]
            [clojure.spec :as s]
            [hphelper.server.shared.spec :as ss]
            [clojure.data.json :as json]
            [hphelper.server.shared.helpers :as help]
            )
  (:gen-class)
)

;; Current games, keyed by uuid
(defonce ^:private current-games
  (atom {}))

;; Current indicies, keyed by zone string
(defonce ^:private current-indicies
  (atom {}))

;; Indicies manipulation
(defn new-game-indicies
  "If the zone doesn't already exist, associates the indicies and returns them.
  If the zone already exists, ignores inds and returns the associated indicies"
  [^String zone inds]
  (log/trace "new-game-indicies." "zone:" zone "inds:" inds "already stored:" (get @current-indicies zone))
  (if-let [i (get @current-indicies zone)]
    ;; Indicies already exist for this zone, get them
    i
    ;; Indicies dont exist
    (-> (swap! current-indicies assoc zone
               ;; If it's a map, put it in a list. Otherwise assume it's a list of maps
               (if (map? inds) (list inds) inds))
        (get zone)
        )
    )
  )

;; Gets the current time in milliseconds
(defn current-time
  "Returns the current time as a long"
  []
  (.getTimeInMillis (java.util.Calendar/getInstance))
  )

;; Changing a stored scenario into a live scenario for online play
(defn- player-setup
  "Adds a uuid key under :password to a player map, performs other live setup, returns the map"
  [[_ pMap]]
  (let [pass (uni/uuid)
        t (current-time)]
    [pass
     (-> pMap
         (assoc :password pass)
         )
     ]
    )
  )
(defn- setup-indicies-history
  "Sets the initial indicies history to a list of the current indicies map"
  [{inds :indicies zone :zone :as sMap}]
  (log/trace "setup-indicies-history.")
  (assoc sMap :indicies (new-game-indicies zone inds)))
(defn- setup-current-access-totals
  "Sets up a map of player names to current access, and starts indicies history"
  [{players :hps :as sMap}]
  (log/trace "setup-current-access-totals. players:" (vals players))
  (let [at (reduce merge {"Misc" 0 "Pool" 0}
                   (map (fn [pMap]
                          (log/trace "pMap:" pMap)
                          {(:name pMap) (:accessRemaining pMap)})
                        (vals players)))]
    (-> sMap
        (assoc :access (list at))
        )
    )
  )
(defn- setup-last-updated
  "Sets up a map of last updated times for all keys"
  [sMap ^Integer t]
  (log/trace "setup-last-updated. time:" t)
  (-> sMap
      (assoc :updated (reduce merge {} (map (fn [k] {k t}) (conj (keys sMap) :missions :hps))))
      )
  )
(defn- player-all-setup
  "Adds a uuid password to all players in a scenMap, returns the map"
  [sMap]
  (-> sMap
      ;; Setup individual players
      (update-in [:hps] (fn [pl] (apply merge {}
                                        (map player-setup pl))))
      ;; Setup indicies history
      setup-indicies-history
      ;; Setup current access totals and access history
      setup-current-access-totals
      ;; Setup last updated to now
      (setup-last-updated (current-time))
      )
  )
(defn new-game
  "Creates a new game, either from an existing map or straight 0s."
  ([valMap]
   {:post (s/valid? ::ss/liveScenario)}
   (uni/add-uuid-atom! current-games
                       (-> valMap
                           ;; Fill in any missing indices
                           (update-in [:indicies]
                                      (partial merge
                                               (indicies/create-base-indicies-list)))
                           ;; Make sure the servicegroups are in a vector
                           (update-in [:serviceGroups]
                                      #(into [] %))
                           ;; Make sure the news is in the correct format
                           (update-in [:news]
                                      (partial concat
                                               '()))
                           ;; Set the admin login
                           (assoc :adminPass (uni/uuid))
                           ;; Adds passwords to everyone
                           (player-all-setup)
                           ;; Make sure cbay exists, even if it's just an empty list
                           (update-in [:cbay] (partial concat []))
                       )))
  ([]
   (new-game {:indicies (indicies/create-base-indicies-list)})))

;; Gets a full game structure by uuid from current-games
(defn get-game
  "Gets the game associated with the uid"
  [^String uid]
  (uni/get-uuid-atom current-games uid))

;; Performs func with the arguments on the uuid game. Returns the modified game
(defn- swap-game!
  "Applys the function to the game associated with the uuid
  Asserts afterwards the game matches spec."
  [uid func & args]
  (log/trace "swap-game! uid:" uid "func:" func)
  (apply uni/swap-uuid! current-games uid (comp #(s/assert ::ss/liveScenario %) func) args))

;; Changes the access amount of an access member
(defn modify-access-inner
  "Modifies an index by a certain amount, and fuzzifies the indices"
  [scenMap player ^Integer amount]
  (log/trace "modify-access-inner. player:" player "amount:" amount "access:" (-> scenMap :access first))
  (let [newAccess (-> (first (:access scenMap))
                      (update-in [player] + amount)
                      )]
    (log/trace "modify-index-inner. newAccess:" newAccess)
    (-> scenMap
        (update-in [:access] (partial cons newAccess))
        (assoc-in [:updated :access] (current-time))
        )
    )
  )
(defn modify-access
  "Modifys an index by a certain amount, returns the map, or nil if the game doesn't exist"
  [^String uid player amount]
  (log/trace "modify-access:" uid player amount)
  (if (string? amount)
    (modify-access uid
                  player
                  (try (Integer/parseInt amount)
                       (catch Exception e ;; If fails to parse, return 0 so it won't do anything
                         (do
                           (log/debug "modify-item: could not parse" amount)
                           0))))
    (if (-> @current-games (get uid) :access first (get player))
      (do
        (log/trace "modify-access. Modifying access.")
        (swap-game! uid modify-access-inner player amount)
        )
      (do
        (log/error "modify-access. Could not find game.")
        nil))
  ))

;; Sends access from one account to another
(defn send-access-inner
  "Actually sends access, adding a single line to access and updating :updated"
  [g ^String player-from ^String player-to ^Integer amount]
  (log/trace "send-access-inner" player-from player-to amount (type amount))
  (let [newAccess (-> (first (:access g))
                      (update-in [player-from] + (- amount))
                      (update-in [player-to] + amount)
                      )]
    (log/trace "send-access-inner. newAccess:" newAccess)
    (-> g
        (update-in [:access] (partial cons newAccess))
        (assoc-in [:updated :access] (current-time))
        )
    )
  )
(defn send-access
  "Sends access from one player to another"
  [^String uid ^String player-from ^String player-to amount]
  (let [g (get-game uid)
        am (help/parse-int amount)]
    (log/trace "send-access" uid player-from player-to "amount:" amount "parsedamount:" am (type am))
    (cond
      ;; game invalid
      (not g)
      nil
      ;; Couldn't parse amount
      (not am)
      nil
      ;; Targets don't exist in the access pool
      (not (and ((-> g :access first keys set) player-from)
                ((-> g :access first keys set) player-to)
                )
           )
      nil
      ;; All is well, do it
      :all-good
      (swap-game! uid send-access-inner player-from player-to am)
      )
    )
  )

;; Modifies a single index. Fuzzifies all indicies, then normalises the service group indicies
(defn modify-index-inner
  "Modifies an index by a certain amount, and fuzzifies the indices"
  [{:keys [zone] :as scenMap} index ^Integer amount]
  (log/trace "modify-index-inner. index:" index "amount:" amount "indicies:" (-> scenMap :indicies first))
  (let [newInds (-> (swap! current-indicies
                           update-in [zone]
                           ;; Functions takes a list of indicies, conjoins a modified index to the beginning
                           (fn [l]
                             (conj l
                                   (-> l
                                       first
                                       (update-in [index] + amount)
                                       indicies/fuzzify-indicies
                                       indicies/normalise-all-indicies
                                       )
                                   )
                             )
                           )
                    ;; We have the result of the swap, now we get the indicies
                    (get zone)
                    )
        ]
    (log/trace "modify-index-inner. newInds:" newInds)
    (-> scenMap
        (assoc-in [:indicies] newInds)
        (assoc-in [:updated :indicies] (current-time))
        )
    )
  )
(defn modify-index
  "Modifys an index by a certain amount, returns the map, or nil if the game doesn't exist"
  [^String uid index amount]
  (log/trace "modify-index:" uid index amount)
  (if (string? amount)
    (modify-index uid
                  index
                  (try (Integer/parseInt amount)
                       (catch Exception e ;; If fails to parse, return 0 so it will just fuzzify
                         (do
                           (log/debug "modify-item: could not parse" amount)
                           0))))
    (if (-> @current-games (get uid) :indicies first index)
      (do
        (log/trace "modify-index. Modifying index.")
        (swap-game! uid modify-index-inner index amount)
        )
      (do
        (log/error "modify-index. Could not find game.")
        nil))
  ))

;; Sets the owner of a service group
(defn- set-sg-owner-inner
  "Actually sets the owner of the service group, along with the updated time"
  [g sgIndex newOwner]
  (log/trace "set-sg-owner-inner. sgIndex:" sgIndex "newOwner:" newOwner)
  (-> g
      (assoc-in [:serviceGroups sgIndex :owner] newOwner)
      (assoc-in [:updated :serviceGroups] (current-time))
      )
  )
(defn set-sg-owner
  "Sets the owner of a service group, returns the sg_id, or nil if failure.
  sg can be either the name, id, or abbreviation"
  [^String uid ^String sg ^String newOwner]
  (log/trace "set-sg-owner:" uid sg newOwner)
  (if-let [index (help/get-sg-index (get-game uid) sg)]
    (do
      (log/trace "set-sg-owner. Found sg index:" index)
      (swap-game! uid set-sg-owner-inner index newOwner)
      )
    (do
      (log/trace "set-sg-owner. Found no index for sg:" sg)
      nil
      )
    )
  )

;; Adds or gets news from a game
(defn add-news-item
  "Adds a single news item to a game"
  [uid ^String newsItem]
  (swap-game! uid update-in [:news] conj newsItem))
(defn get-news
  "Gets the news list of a game"
  [uid]
  ((uni/get-uuid-atom current-games uid) :news))

;; Lets a player purchase a minion. Automatically adjusts their access total
(defn- set-minion-bought-status
  "Sets a minion's status to bought or not"
  [g ^Integer sgid ^Integer minionid bought?]
  (log/trace "set-minion-bought-status:" sgid minionid bought?)
  (let [sgindex (help/get-sg-index g sgid)]
    (log/trace "sgindex:" sgindex)
    (-> g
        (update-in [:serviceGroups (help/get-sg-index g sgid) :minions]
                   (fn [ms]
                     (doall
                       (map (fn [{minion_id :minion_id :as m}]
                              (if (= minion_id minionid)
                                ;; Minion we're after
                                (let [ret (assoc m :bought? bought?)]
                                  (log/trace "bought minion:" ret)
                                  ret
                                  )
                                ;; Not after this one
                                (do
                                  ;(log/trace "Didn't edit minion:" m)
                                  m
                                  )
                                )
                              )
                            ms
                            )
                       )
                     )
                   )
        (assoc-in [:updated :serviceGroups] (current-time))
        )
    )
  )
(defn purchase-minion-inner
  "Actually purchases the minion for the player."
  [g ^String player ^Integer sgid ^Integer minionid ^Integer cost]
  (log/trace "purchase-minion-inner:" player sgid minionid cost)
  (-> g
      (set-minion-bought-status sgid minionid true)
      (modify-access-inner player (- cost))
      )
  )
(defn purchase-minion
  "Has a player purchase a minion. Deducting the required amount of access, and setting :bought on the minion"
  [^String uid ^String player ^Integer sgid ^Integer minionid]
  (log/trace "purchase-minion:" uid player sgid minionid)
  (let [g (get-game uid)
        minion (->> g
                    :serviceGroups
                    ;; Get the service group
                    (some (fn [{sg_id :sg_id :as sg}]
                            (if (= sgid sg_id)
                              sg
                              nil)))
                    ;; We have the service group, now to get the minion
                    :minions
                    (some (fn [{minion_id :minion_id :as m}]
                            (if (= minion_id minionid)
                              m
                              nil)))
                    )
        ]
    (cond
      ;; Does the minion exist?
      (not minion)
      (do (log/trace "Minion does not exist.") nil)
      ;; Is the minion already purchased?
      (:bought? minion)
      (do (log/trace "Minion is already bought.") nil)
      ;; Does the character not exist?
      (not (help/is-hp-name? g player))
      (do (log/trace "Character" player "does not exist.") nil)
      ;; All seems well
      :all-well
      (swap-game! uid purchase-minion-inner player sgid minionid (:minion_cost minion))
      )
    )
  )
