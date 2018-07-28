(ns hphelper.server.shared.helpers
  (:require [clojure.spec.alpha :as s]
            [hphelper.server.shared.spec :as ss]
            [taoensso.timbre :as log]
            )
  (:gen-class)
  )

;;; Legacy code, left for reference. TODO delete
(defn- get-sg-index-old
  "Finds the index of a service group in a game object. Returns the index, or nil if none
  Searches by sg_id, sg_abbr, or sg_name"
  [g ^String n]
  (first
    (keep-indexed
      (fn [index i]
        ;(log/trace "Searching at index" index "in item" i)
        (if (or (= n (:sg_abbr i))
                (= n (:sg_name i))
                (= (str n) (str (:sg_id i)))
                )
          index
          nil
          )
        )
      (:serviceGroups g)
      )
    )
  )

(defn get-sg-abbr
  "Searches the game for the abbr of a service group. Returns the abbr, or nil if none.
  Searches by sg_id, sg_abbr, or sg_name"
  [g s]
  (log/trace "get-sg-abbr. s:" s)
  (some (fn [[_ {:keys [sg_id sg_abbr sg_name]}]]
          (log/trace "get-sg-abbr. sg_id:" sg_id "sg_abbr:" sg_abbr "sg_name:" sg_name)
          (if (or (= (str s) (str sg_id))
                  (= s sg_abbr)
                  (= s sg_name))
            sg_abbr
            nil))
        (:serviceGroups g)))

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

(defn parse-int
  "Parses an integer. Returns the integer, or nil if invalid"
  [i]
  (try (int (Integer/parseInt i))
       (catch NumberFormatException e
         (log/trace "Could not parse:" i)
         nil
         )
       )
  )

(defn get-player-uid
  "Given a game and a player name, will return the uid for that player, or null if that player doesn't exist"
  [scenMap playerName]
  (some (fn [[pass {p-name :name}]]
          (if (= playerName p-name) pass nil))
        (:hps scenMap)))
