(ns hphelper.server.chargen.generator
  (:require [hphelper.server.shared.sql :as sql]
            [hiccup.core :refer :all]
            [taoensso.timbre :as log]
            [hphelper.server.chargen.print :refer :all]
    )
  )

(defn- random-stats
  "Creates a map of the 6 stats with random values"
  []
  (let [stats ["Violence" "Management" "Subterfuge" "Wetware" "Software" "Hardware"]]
    (reduce (fn [x y] (merge x {y (inc (rand-int 20))})) {} stats)))

(defn- calc-primary-stats
  "Checks a record for existing primary stats, randomly fills in empty stats"
  [charRec]
  (update-in charRec [:priStats] (partial merge (random-stats))))

(defn- check-for-minimum-management
  "If there is no management skill but there are societies picked, make sure management is at a minimum"
  [charRec]
  (if (and (not (-> charRec :priStats (get "Management")))
           (-> charRec (get :programGroup))) ;; If no management skill but an existing program group
    (assoc-in charRec [:priStats "Management"]
              (let [minimum (inc
                              (* 5
                                 (+ -2
                                    (min 5
                                         (count (get charRec :programGroup))))))]
                (log/info "Minimum is: " minimum)
                (+ minimum (rand-int (- 21 minimum)))))
    charRec))

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
  [{pg :programGroup {pgs "Program Group Size"} :secStats :as charRec}]
  (if (<= (count pg)
          pgs) ;; If there's too many societies, drop one below
    (if (>= (-> charRec :programGroup (count))
            pgs)
      ;; We have the right number of groups
      charRec
      (recur (assoc charRec :programGroup ;; Add a random society
                    (clojure.set/union #{} pg #{(sql/get-random-society)}))))
    (recur (assoc charRec :programGroup ;; Drop a random society
                  (disj pg (rand-nth (into '() pg)))))))

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
  [charRec ^Integer minimumAccess]
  (if (and
        (> (calc-access-remaining charRec) minimumAccess)
        (not (charRec :publicStanding))
        ;; Has a 50% chance of happening if the above two are correct
        (> 0.5 (Math/random)))
    (assoc-in charRec [:publicStanding]
              (+ (int (Math/ceil (* (Math/random) 5))) 5)) ;; Creates a public standing between 6 and 10
    charRec ;; Don't do anything, just return the record
    ))

(defn- set-remaining-access
  "Sets the access remaining in the character record"
  [charRec]
  (if (:accessRemaining charRec)
    ;; From web api. Sums have changed slightly, so let's not recalculate it
    charRec
    ;; From old, need to calculate
    (assoc charRec :accessRemaining (calc-access-remaining charRec))
    )
  )

(defn create-drawback
  "Creates a single drawback in a character record"
  [charRec]
  (assoc-in charRec [:drawbacks] (clojure.set/union #{}
                                                    (charRec :drawbacks)
                                                    #{(sql/get-random-drawback)})))

(defn- create-drawbacks
  "Checks minimum access.
  If not enough, adds a drawback up to three and adds access.
  If above minimum access, has a small chance of adding another drawback"
  [charRec ^Integer minimumAccess]
  (log/trace "create-drawbacks. drawbackCount:" (:drawbackCount charRec) "drawbacks:" (:drawbacks charRec))
  (if-let [c (:drawbackCount charRec)]
    ;; From the web api, generate drawbacks. Overwrites any ones that may already be here
    (-> charRec
        (assoc-in [:drawbacks]
                  (->> (sql/get-drawback-all)
                       shuffle
                       (take c)))
        (dissoc :drawbackCount)
        )
    ;; As per normal, check spent access
    (if (>= (count (charRec :drawbacks)) 3)
      charRec ;; Already at 3 drawbacks
      (if (< (calc-access-remaining charRec) minimumAccess)
        (recur (create-drawback charRec) minimumAccess)
        charRec)
      )
    )
  )

(defn- create-mutation
  "Creates a mutation at the specified power level."
  [charRec ^Integer powerLevel]
  (log/trace "create-mutation. desc:" (get-in charRec [:mutation :description]) "total:" (get-in charRec [:mutation :total]))
  (log/trace "mutation total:" (:mutation charRec))
  (if (get-in charRec [:mutation :description])
    charRec
    (if-let [t (get-in charRec [:mutation :total])]
      ;; From api
      (assoc-in charRec [:mutation :description]
                (->> (sql/get-mutation-all) shuffle (take t)))
      ;; From old
      (let [mut (sql/get-random-mutation)]
        (assoc charRec :mutation {:description (str (mut :name) ": " (mut :desc)) :power powerLevel})))))

(defn- check-name
  "Makes sure the character has a name, even if it is unnamed"
  ([{^String nam :name :as charRec}]
   (if nam
     charRec
     (assoc-in charRec [:name]
               (sql/get-random-name "U")))))

(defn create-character
  "Fills in the missing spots in a character record.
  If no record is given, creates a completely random character sheet"
  ([] (create-character {}))
  ([charRec]
   (log/trace "create-character:" charRec)
   (if charRec ;; In case of nils
     (-> charRec
         (check-for-minimum-management)
         (calc-primary-stats)
         (calc-clone-degredation)
         (calc-program-group-size)
         (create-societies)
         ;(create-public-standing 30) ; No longer required
         (create-drawbacks 0)
         (create-mutation 10)
         (set-remaining-access)
         (check-name)
         )
     (recur {}))
   )
  )
