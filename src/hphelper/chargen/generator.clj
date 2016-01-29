(ns hphelper.chargen.generator
  (:require [clojure.core.typed :as t]
            [hphelper.shared.sql :as sql]
            [hiccup.core :refer :all]
    )
  )

(defn- random-stats
  "Creates a map of the 6 stats with random values"
  []
  (let [stats ["Violence" "Management" "Subterfuge" "Wetware" "Software" "Hardware"]]
    (reduce (fn [x y] (merge x {y (int (Math/ceil (* (Math/random) 20)))})) {} stats)))

(defn- calc-primary-stats
  "Checks a record for existing primary stats, randomly fills in empty stats"
  [charRec]
  (assoc-in charRec [:priStats] (merge (random-stats) (charRec :priStats))))

(defn- calc-clone-degredation
  "Calculates clone degredation in a character record, deriving from stats"
  [charRec]
  (assert (charRec :priStats) "Can't derive without stats")
  (assoc-in charRec 
            [:secStats "Clone Degredation"] 
            (int (- 5 (Math/ceil (/ ((charRec :priStats) "Wetware") 5))))))

(defn- calc-program-group-size
  "Calculates program group size in a character record, derived from stats"
  [charRec]
  (assert (charRec :priStats) "Can't derive without stats")
  (assoc-in charRec
            [:secStats "Program Group Size"]
            (int (inc (Math/ceil (/ ((charRec :priStats) "Management") 5))))))

(defn- create-societies
  "Adds program group socities to the record from the database"
  [charRec]
  (assert (<= (count (charRec "Program Group")) 
              (-> charRec (:secStats) (get "Program Group Size")))
          "Secret society count greater than allowed")
  (if (>= (-> charRec (get "Program Group") (count))
          (-> charRec (:secStats) (get "Program Group Size")))
    charRec
    (recur (assoc-in charRec ["Program Group"] (clojure.set/union #{} (charRec "Program Group") #{(sql/get-random-society)})))))

(defn- calc-access-remaining
  "Calculates remaining access"
  [charRec]
  (- 100
     (+ (reduce + (vals (charRec :priStats)))
        (if (number? (charRec :publicStanding))
          (* 2 (charRec :publicStanding))
          0)
        (- (* 10 ;; Reduces used amounts by 10 * drawbacks
              (if (charRec :drawbacks)
                (count (charRec :drawbacks))
                0))))))

(defn- create-public-standing
  "Checks remaining access, and if more than specified has a chance to give the character a good public standing."
  [charRec minimumAccess]
  (if (and 
        (> (calc-access-remaining charRec) minimumAccess)
        (not (charRec :publicStanding))
        (> 0.5 (Math/random)))
    (assoc-in charRec [:publicStanding] 
              (+ (int (Math/ceil (* (Math/random) 5))) 5)) ;; Creates a public standing between 6 and 10
    charRec ;; Don't do anything, just return the record
    ))

(defn- set-remaining-access
  "Sets the access remaining in the character record"
  [charRec]
  (assoc charRec :accessRemaining (calc-access-remaining charRec)))

(defn- create-drawbacks
  "Checks minimum access.
  If not enough, adds a drawback up to three and adds access.
  If above minimum access, has a small chance of adding another drawback"
  [charRec minimumAccess]
  (if (>= (count (charRec :drawbacks)) 3)
    charRec ;; Already at 3 drawbacks
    (if (or (< (calc-access-remaining charRec) minimumAccess)
            (< (Math/random) 0.2)) ;; 1 in 5 chance of an extra drawback
      (let [newCharRec (assoc-in charRec [:drawbacks] (clojure.set/union #{}
                                                                          (charRec :drawbacks)
                                                                          #{(sql/get-random-drawback)}))]
        (recur newCharRec minimumAccess))
      charRec)))

(defn- create-mutation
  "Creates a mutation at the specified power level."
  [charRec powerLevel]
  (if (charRec :mutation)
    charRec
    (let [mut (sql/get-random-mutation)]
      (assoc charRec :mutation {:description (str (mut :name) ": " (mut :desc)) :power powerLevel}))))

(defn- check-name
  "Makes sure the character has a name, even if it is unnamed"
  ([{nam :name :as charRec}]
   (if nam
     charRec
     (assoc-in charRec [:name]
               (sql/get-random-name)))))

(defn create-character
  "Fills in the missing spots in a character record.
  If no record is given, creates a completely random character sheet"
  ([] (create-character {}))
  ([charRec] (-> charRec
                 (calc-primary-stats)
                 (calc-clone-degredation)
                 (calc-program-group-size)
                 (create-societies)
                 (create-public-standing 30)
                 (create-drawbacks 30)
                 (create-mutation 10)
                 (set-remaining-access)
                 (check-name)
                 )
   )
  )


;; Displaying the Character
(defn html-print-stats
  "Returns the primary stats of a character in a readable format"
  [charRec]
  (html [:div 
            [:b "Primary Statistics"]
            [:table {:style "width:100%"}
             (for [row (partition-all 2 (charRec :priStats))]
               [:tr (for [cell row] [:td (key cell) ": " (val cell)])])]]
           ))

(defn html-print-secondary
  "Returns the secondary stats of a character in a readable format"
  [charRec]
  (html [:div
            [:b "Secondary Statistics"][:br]
            (interpose (html [:br]) (map (fn [[k v]] (str k ": " v)) (charRec :secStats)))]
           ))

(defn html-print-mutation
  "Returns the mutation of a character in a readable format"
  [charRec]
  (html [:div
            (str "Mutation Description: "
                 (-> charRec (:mutation) (:description))
                 (html [:br])
                 "Mutation Strength: "
                 (-> charRec (:mutation) (:power)))
            ]
           ))

(defn html-print-program-group
  "Returns the program group of a character in a readable format"
  [charRec]
  (html [:div
            [:b "Program Group"][:br]
            [:table {:style "width:100%"}
             [:tr [:td "Society"] [:td "Skills"] [:td "Cover Identity"]]
             (for [ss (charRec "Program Group")] 
               [:tr 
                [:td (ss :ss_name)]
                [:td [:small (ss :sskills)]]])]
            ]
           ))

(defn html-print-public-standing
  "Returns the public standing of a character in a readable format"
  [charRec]
  (html
    [:div
     "Public Standing: "
     (if (charRec :publicStanding)
       (charRec :publicStanding)
       "None")
     ]
    ))

(defn html-print-drawbacks
  "Returns the drawbacks of a character in a readable format"
  [charRec]
  (if (charRec :drawbacks)
    (html [:div
              [:b "Drawbacks"][:br]
               (for [drawback (charRec :drawbacks)]
                 (str drawback (html [:br])))
               ])
    ""))

(defn html-print-service-groups
  "Prints a list of the service groups in a readable format"
  []
  (html
    [:div
     [:b "Service Group Bids"][:br]
      (for [group (sql/query "SELECT `sg_name` FROM `sg` ORDER BY `sg_name` ASC;")]
        (str (group :sg_name) "<br />"))
      ]
    ))

(defn html-print-remaining-access
  "Prints the remaining access in a readable format"
  [charRec]
  (html
    [:div
     [:b "Remaining Access: "]
     (charRec :accessRemaining)
     ]))

(defn html-print-name
  "Prints the name of the character in a readable format"
  [charRec]
  (html
    [:div
     [:large [:b
              (if (charRec :name)
                (charRec :name)
                "Unnamed-U-ARE")
              ]]
     ]
    ))

(defn html-print-sheet
  "Prints a character sheet in a readable format"
  [charRec]
  (html 
    [:html 
     (html-print-name charRec)
     (html-print-stats charRec) 
     (html-print-secondary charRec)    
     (html-print-mutation charRec)
     (html-print-public-standing charRec)
     (html-print-program-group charRec)
     (html-print-drawbacks charRec)
     (html-print-service-groups)
     (html-print-remaining-access charRec)
     ]))

(html-print-sheet (create-character))
