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

;; If currentGames exists, and is an atom, leave it alone
(defonce currentGames
  (atom {}))

(defn current-time
  "Returns the current time as a long"
  []
  (.getTimeInMillis (java.util.Calendar/getInstance))
  )

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
  [{inds :indicies :as sMap}]
  (log/trace "setup-indicies-history.")
  (assoc sMap :indicies (list inds)))
(defn- setup-current-access-totals
  "Sets up a map of player names to current access, and starts indicies history"
  [{players :hps :as sMap}]
  (log/trace "setup-current-access-totals. players:" (vals players))
  (let [at (reduce merge {} (map (fn [pMap]
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
      (assoc :updated (reduce merge {} (map (fn [k] {k t}) (keys sMap))))
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
   (uni/add-uuid-atom! currentGames
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

(defn get-game
  "Gets the game associated with the uid"
  [^String uid]
  (uni/get-uuid-atom currentGames uid))

(defn- swap-game!
  "Applys the function to the game associated with the uuid
  Asserts afterwards the game matches spec."
  [uid func & args]
  (log/trace "swap-game! uid:" uid "func:" func)
  (apply uni/swap-uuid! currentGames uid (comp #(s/assert ::ss/liveScenario %) func) args))

(defn modify-index-inner
  "Modifies an index by a certain amount, and fuzzifies the indices"
  [scenMap index ^Integer amount]
  (log/trace "modify-index-inner. index:" index "amount:" amount "indicies:" (-> scenMap :indicies first))
  (let [newInds (-> (first (:indicies scenMap))
                    (update-in [index] + amount)
                    indicies/fuzzify-indicies
                    indicies/normalise-all-indicies
                    )]
    (log/trace "modify-index-inner. newInds:" newInds)
    (-> scenMap
        (update-in [:indicies] (partial cons newInds))
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
    (if (-> @currentGames (get uid) :indicies first index)
      (do
        (log/trace "modify-index. Modifying index.")
        (swap-game! uid modify-index-inner index amount)
        )
      (do
        (log/error "modify-index. Could not find game.")
        nil))
  ))

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

(defn add-news-item
  "Adds a single news item to a game"
  [uid ^String newsItem]
  (swap-game! uid update-in [:news] conj newsItem))
(defn get-news
  "Gets the news list of a game"
  [uid]
  ((uni/get-uuid-atom currentGames uid) :news))

