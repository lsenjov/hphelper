(ns hphelper.live.control
  (:require [hphelper.shared.sql :as sql]
            [hphelper.shared.saveload :as sl]
            [hphelper.shared.unique :as uni]
            [hphelper.shared.indicies :as indicies]
            ;; For indicies
            [hphelper.scengen.generator :as sgen]
            [clojure.tools.logging :as log]
            )
)

(def currentGames (atom {}))

(defn new-game
  "Creates a new game, either from an existing map or straight 0s. Returns the generated uid"
  ([valMap]
   (uni/add-uuid-atom! currentGames {:indicies valMap}))
  ([]
   (new-game (indicies/create-base-indicies-list))))

(defn get-game
  "Gets the game associated with the uid"
  [uid]
  (uni/get-uuid-atom currentGames uid))

(defn modify-index
  "Modifys an index by a certain amount"
  [uid index amount]
  (if (string? amount)
    (modify-index uid
                  string
                  (try (Integer/parseInt amount)
                       (catch Exception e ;; If fails to parse, return 0 so it will just fuzzify
                         (do
                           (log/debug "modify-item: could not parse" amount)
                           0))))
    (
  ) ;; TODO
