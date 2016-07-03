(ns hphelper.live.control
  (:require [hphelper.shared.sql :as sql]
            [hphelper.shared.saveload :as sl]
            [hphelper.shared.unique :as uni]
            [hphelper.shared.indicies :as indicies]
            ;; For indicies
            [hphelper.scengen.generator :as sgen]
            [taoensso.timbre :as log]
            [schema.core :as s]
            )
  (:gen-class)
)

(def currentGames (atom {}))

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
                       )))
  ([]
   (new-game {:indicies (indicies/create-base-indicies-list)})))

(defn get-game
  "Gets the game associated with the uid"
  [^String uid]
  (uni/get-uuid-atom currentGames uid))

(defn modify-index-inner
  "Modifies an index by a certain amount, and fuzzifies the indices"
  [scenMap index ^Integer amount]
  (-> scenMap
      (update-in [:indicies index] + amount)
      (update-in [:indicies] (comp indicies/normalise-all-indicies indicies/fuzzify-indicies))
      ))

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
    (if (get-in @currentGames [uid :indicies index])
      (uni/swap-uuid! currentGames uid modify-index-inner index amount)
      nil)
  ))

(defn add-news-item
  "Adds a single news item to a game"
  [uid ^String newsItem]
  (uni/swap-uuid! currentGames uid update-in [:news] conj newsItem))

(defn get-news
  "Gets the news list of a game"
  [uid]
  ((uni/get-uuid-atom currentGames uid) :news))
