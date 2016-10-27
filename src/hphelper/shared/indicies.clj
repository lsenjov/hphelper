(ns hphelper.shared.indicies
  (:require [hphelper.shared.sql :as sql]
            [taoensso.timbre :as log]
            )
  (:gen-class)
)

(def sectorIndicies
  "The four sector indicies: Happiness, Compliance, Loyalty, and Security"
  [:HI :CI :LI :SI])

(defn html-print-single-index
  "Prints a single index in html format"
  [index]
  (str (name (key index))
       \space
       (val index)
       \space
       ))

(defn html-print-indicies
  "Prints the indicies in html format"
  [inds]
  (concat (sort (map html-print-single-index
                     (filter (partial some
                                      (into #{}
                                            sectorIndicies))
                             inds)))
          (sort (map html-print-single-index
                     (remove (partial some
                                      (into #{}
                                            sectorIndicies))
                             inds)))))

(defn create-base-indicies-list
  "Combines the base indicies with sg indicies, sets sector indices to 0 and sg indicies to random number between -70 and 70"
  []
  (merge
   (reduce merge (map (fn [k] {k 0}) sectorIndicies))
   (reduce merge (map (fn [k] {k (- (rand-int 141) 70)}) (map (comp keyword :sg_abbr)
                                          (sql/query "SELECT sg_abbr FROM sg"))))))

(defn normalise-specific-indicies
  "Averages the specified indicies in a map"
  [choices indicies]
  ;; Take the negative average of all the chosen indicies
  (let [avg
        (- (int (Math/floor (/ (reduce + ;; Get the negative integer average
                           (map val ;; Get their values
                                (filter (partial some (into #{} choices))
                                        indicies))) ;; Get all the indicies in the choices
                   (count choices)))))]
    ;; Apply to all the keywords
    ((apply comp
            (map (fn [kw]
                   (fn [ind]
                     (update-in ind [kw] + avg)))
                 choices)) indicies)))

(defn normalise-all-indicies
  "Normalises the sector indicies, and the service group indicies"
  [indicies]
  (log/trace "normalise-all-indicies:" indicies)
  (->> indicies
      (normalise-specific-indicies (map key (remove (partial some (into #{} sectorIndicies)) indicies)))))

(defn fuzzify-indicies
  "Randomly adds, subtracts, or leaves alone each of the indicies"
  [indicies]
  ((apply comp (map (fn [kw] (fn [ind]
                               (update-in ind [kw] + (dec (rand-int 3)))))
                    (keys indicies))
          )
   indicies))

(defn adjust-index
  "Using a three letter input, first two letters as keyword and final as direction
  (U for up, D for down), adjusts the selected index.
  If no value is given, defaults to 4"
  ([adj indicies]
   (adjust-index adj indicies 4))
  ([adj indicies value]
   (assert (string? adj) (str "Index adjustment is not a string, is instead " adj))
   (if (or (not (= (count adj) 3))
           (not (or (= (nth adj 2) \D)
                    (= (nth adj 2) \U)))
           (not (get indicies (keyword (.substring adj 0 2))))
           ) ;; TODO Test to see if keyword exists
     (do ; Invalid adjustment, log and return
         (log/debug "Adjustment " adj " incorrect, skipping")
         indicies)
     (update-in indicies
                [(keyword (.substring adj 0 2))]
                +
                (if (= (nth adj 2) \U)
                  value
                  (- value))))))
