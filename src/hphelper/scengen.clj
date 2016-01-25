(ns hphelper.scengen
  (:require [clojure.core.typed :as t]
            [hphelper.sql :as sql]
            [hiccup.core :refer :all]
    )
  )

(defn- get-crisis-id-list
  "Taking a full crisis record, returns a collection of the crisis ids"
  [{crisises :crisises :as crisRec}]
  (map (partial :c_id)
       crisises))

(defn- add-crisis-desc
  "Given a crisis map, adds an :extraDesc field with a vector of string descriptors"
  [{id :c_id :as crisisMap}]
  (assoc-in crisisMap [:extraDesc]
            (reduce conj
                    []
                    (map (partial :ct_desc)
                         (sql/query "SELECT `ct_desc` FROM `crisis_text` WHERE `c_id` = ?;"
                                    id)))))

(defn- add-crisis-descriptions
  "Given crisises, adds extra description to each"
  [{crisises :crisises :as crisRec}]
  (assert crisises)
  (assoc-in crisRec [:crisises]
            (map add-crisis-desc crisises)))

(defn- select-crisises
  "Determines or fills in the crisises to be visited"
  [{crisises :crisises :as crisRec}]
  (if (nil? crisises)
    (recur (assoc-in crisRec [:crisises] #{}))
    (if (vector? crisises)
      (recur (assoc-in crisRec [:crisises]
                       (remove nil? (set (map (fn [id] (first (sql/query "SELECT * FROM `crisis` WHERE `c_id` = ?;" id)))
                                    crisises)))))
      (if (< (count crisises) 3)
        (recur (assoc-in crisRec [:crisises]
                         (set (remove nil? (conj crisises (sql/get-random-row "crisis" "c_id"))))))
        crisRec
        ))))

(defn create-scenario
  "Creates a generated scenario"
  ([] (create-scenario {}))
  ([scen]
   (-> scen
       (select-crisises)
       (add-crisis-descriptions)
       )))
