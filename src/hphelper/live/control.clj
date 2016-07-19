(ns hphelper.live.control
  (:require [hphelper.shared.sql :as sql]
            [hphelper.shared.saveload :as sl]
            [hphelper.shared.unique :as uni]
            [hphelper.shared.indicies :as indicies]
            [taoensso.timbre :as log]
            [schema.core :as s]
            [clojure.data.json :as json]
            )
  (:gen-class)
)

;; If currentGames exists, and is an atom, leave it alone
(def currentGames (if (and (resolve 'currentGames)
                           ;; This line is needed, as during the def currentGames is added to the symbol table,
                           ;; but as an unbound. Need to make sure it's actually an atom
                           (instance? clojure.lang.Atom currentGames))
                    currentGames
                    (atom {})))

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
  "Creates a new game, either from an existing map or straight 0s. Returns the generated uid"
  ([valMap]
   (uni/add-uuid-atom! currentGames
                       (-> valMap
                           ;; Fill in any missing indices
                           (update-in [:indicies]
                                      (partial merge
                                               (indicies/create-base-indicies-list)))
                           ;; Make sure the news is in the correct format
                           (update-in [:news]
                                      (partial concat
                                               '()))
                           (assoc :adminPass (uni/uuid))
                           (player-all-setup)
                       )))
  ([]
   (new-game {:indicies (indicies/create-base-indicies-list)})))

(defn get-game
  "Gets the game associated with the uid"
  [^String uid]
  (uni/get-uuid-atom currentGames uid))

(def swap-game!
  "Applys the function to the game associated with the uuid
  ([uid fn & args])"
  (partial uni/swap-uuid! currentGames))

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
        (uni/swap-uuid! currentGames uid modify-index-inner index amount)
        )
      (do
        (log/error "modify-index. Could not find game.")
        nil))
  ))

(defn add-news-item
  "Adds a single news item to a game"
  [uid ^String newsItem]
  (uni/swap-uuid! currentGames uid update-in [:news] conj newsItem))

(defn get-news
  "Gets the news list of a game"
  [uid]
  ((uni/get-uuid-atom currentGames uid) :news))

