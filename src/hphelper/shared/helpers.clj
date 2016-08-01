(ns hphelper.shared.helpers
  (:require [clojure.spec :as s]
            [hphelper.shared.spec :as ss]
            [taoensso.timbre :as log]
            )
  (:gen-class)
  )

(defn is-hp-name?
  "Checks a game to make sure n is a valid player's name. Returns the name if true,
  else returns nil."
  [g ^String n]
  (let [names (->> g :hps vals (map :name) (into []))]
    (log/trace "is-hp-name?" (pr-str n) "in:" names)
    (log/trace "is-hp-name?" (hash n) "in:" (into [] (map hash names)))
    (if (some #{n} names)
      (do (log/trace "is-hp-name? returned true.") n)
      (do (log/trace "is-hp-name? returned false.") nil)
      )
    )
  )
