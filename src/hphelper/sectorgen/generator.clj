(ns hphelper.sectorgen.generator
  (:require [hphelper.shared.sql :as sql]
            [hiccup.core :refer :all]
            [taoensso.timbre :as log]
            ))

(defn create-service-group
  [sgId]
  {sgId (sql/get-random-name "V")})

(defn create-sector
  "Creates the High Programmers and service group heads of a sector"
  []
  {
   :sgheads (reduce merge
                    (map (comp create-service-group :sg_id)
                         (sql/query "SELECT * FROM sg;")))
   :hps (reduce conj [] (repeatedly 6 (partial sql/get-random-name "U")))
   :zone (sql/create-random-zone-name)
   })

(defn print-sg-head
  "Prints the sg head in a nice format"
  [[k v :as sgh]]
  (str (sql/get-sg-by-id k) ": " v))

(defn html-print-sector
  "Prints a single sector in a nice format"
  [sec]
  (html [:div
         [:b "Zone:" (sec :zone) [:br]]
         [:b "High Programmers:"] [:br]
         (interpose [:br] (sort (sec :hps)))
         [:br]
         [:b "SG Heads"] [:br]
         (interpose [:br] (sort (map print-sg-head (sec :sgheads))))
         ]))

(defn html-print-neighbours
  "Prints a number of neighbouring randomly generated neighbours"
  [n]
  (html (map html-print-sector
             (repeatedly n create-sector))))
