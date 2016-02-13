(ns hphelper.sectorgen.generator
  (:require [hphelper.shared.sql :as sql]
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
   })
