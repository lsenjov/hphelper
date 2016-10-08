(ns hphelper.shared.sgspecialties
  (:require [taoensso.timbre :as log]
            [hphelper.shared.sql :as sql]
            [clojure.spec :as s]
            [hphelper.shared.spec :as ss]
            ))

(def ^:private chance
  "The chance of a skill being given a bonus or being dropped"
  0.3
  )

(def ^:private clearance-layers
  {:low ["IR" "R" "O"]
   :med ["Y" "G" "B"]
   :high ["I" "V"]}
  )

(def ^:private clearance-costs
  {"IR" 0
   "R" 1
   "O" 2
   "Y" 3
   "G" 4
   "B" 5
   "I" 6
   "V" 7
   }
  )

(defn- get-sg-skills
  "Returns a list of skills of a service group"
  [sgid]
  {:pre [(s/valid? integer? sgid)]
   :post [(s/valid? (s/coll-of (s/map-of keyword? string?)) %)]
   }
  (->> sgid
       (sql/query "SELECT skills.skills_name, skills.skills_parent
                  FROM skills, sg_skill
                  WHERE sg_skill.sg_id = ?
                  AND sg_skill.skills_id = skills.skills_id;"
                  )
       )
  )

(defn- add-skill
  "Random chance to randomly add a skill and increase the cost of the minion"
  [minion]
  (log/trace "add-skill:" minion)
  ;; Randomly add a skill
  (if (< (rand) chance)
    (-> minion
        (assoc-in [:mskills (sql/get-random-skill)] 1)
        (update-in [:minion_cost] (partial + 2))
        (#(do (log/trace "add-skill: added skill, returning:" %) %))
        )
    minion
    )
  )

(defn- adjust-skill-list
  "Drops skills or increases specialties according to chance,
  may also add other, non-core skills"
  [sl]
  (log/trace "adjust-skill-list:" sl)
  (->> sl
       ;; Randomly drop a skill
       (#(if (and (< (rand) chance)
                  (> (count %) 1)
                  )
           ;; Drop a skill
           (do
             (log/trace "dropping skill")
             (dissoc % (rand-nth (keys %)))
             )
           ;; Leave alone
           %
           )
             )
       ;; Randomly increase a skill
       (map (fn [[k v]]
              (if (< (rand) chance)
                (do (log/trace "incrementing skill:" k)
                    [k (inc v)])
                [k v])
              )
            )
       (apply merge {})
       (#(do (log/trace "adjust-skill-list: returning:" %) %))
       )
  )

(defn- convert-skill-list
  "Converts a map of skill and frequency to a string"
  [sl]
  (log/trace "convert-skill-list:" sl)
  (->> sl
       ;; Convert to a string
       (map (fn [[{:keys [skills_name skills_parent]} v]]
              (log/trace "Converting skill:" skills_name
                         "with value:" v)
              (str skills_name
                   ;; Add the bonus if greater than 1
                   (if (> v 1)
                     (str " +" (* 4 (dec v)))
                     "")
                   ;; Add a space
                   " "
                   ;; If the parent skill isn't other, print it in parens
                   (if (not (= skills_parent "O"))
                     (str "(" skills_parent ")"
                          )
                     ""
                     )
                   )
              )
            )
       ;; Sort skills in alphabetical order
       sort
       ;; Make it pretty
       (interpose ", ")
       (apply str))
  )

(defn- convert-mskills
  "Converts :mskills from a list of maps to a string"
  [mlist]
  (log/trace "convert-mskills:" mlist)
  (-> mlist
      (update-in [:mskills] frequencies)
      ;; Increase or drop skills
      (update-in [:mskills] adjust-skill-list)
      (#(assoc % :minion_cost
               (+ (reduce + (vals (:mskills %)))
                  (get clearance-costs (:minion_clearance %))
                  )
               ))

      ;; Possibly add a new skill
      add-skill

      ;; Convert to string
      (update-in [:mskills] convert-skill-list)
      )
  )

(defn- create-specialty-combinations
  "Creates a specialty list. Length is how many items, width is how many of each specialty"
  ([slist width length]
   {:pre [(s/assert (s/coll-of map?) slist)]}
   (->> (repeat slist)
        flatten
        (take (* length width))
        shuffle
        (partition width)
        )
   )
  ([slist width]
   (create-specialty-combinations slist width
                                  (inc (quot (count slist) width)))
   )
  )

(defn- create-minion-list-low
  "From a specialty list, creates a low clearance list of minions (no names)"
  [slist]
  {:pre [(s/valid? (s/coll-of (s/map-of keyword? string?)) slist)]
   }
  (map (fn [sk] {:minion_clearance (rand-nth (:low clearance-layers))
                 :mskills (list sk)})
       slist
       )
  )

(defn- create-minion-list-medium
  "From a specialty list, creates a medium clearance list of minions (no names)"
  [slist]
  {:pre [(s/valid? (s/coll-of (s/map-of keyword? string?)) slist)]
   }
  (map (fn [sk] {:minion_clearance (rand-nth (:med clearance-layers))
                 :mskills sk})
       (create-specialty-combinations slist 2)
       )
  )

(defn- create-minion-list-high
  "From a specialty list, creates a medium clearance list of minions (no names)"
  [slist]
  {:pre [(s/valid? (s/coll-of (s/map-of keyword? string?)) slist)]
   }
  (map (fn [sk] {:minion_clearance (rand-nth (:high clearance-layers))
                 :mskills sk})
       (create-specialty-combinations slist 3)
       )
  )

(defn create-minion-list-for-sg
  "From a service group id, create a full minion list"
  [sgid]
  (let [sgskills (get-sg-skills sgid)]
    (->> (concat (create-minion-list-low sgskills)
                 (create-minion-list-medium sgskills)
                 (create-minion-list-high sgskills))
         (map convert-mskills)
         (map #(assoc % :minion_name "No Name"))
         )
    )
  )

(defn minion-list-to-csv
  "Converts a list of service groups with minions to a csv list"
  [sglist]
  (map (fn [{:keys [sg_id sg_name sg_abbr minions]}]
         ;; Service group header
         (str sg_name \newline
              (apply str
                     ;; Print each minion
                     (map (fn [{:keys [minion_cost minion_clearance mskills]}]
                            (str minion_clearance ","
                                 minion_cost ","
                                 mskills \newline)
                            )
                          minions
                          )
                     )
              )
         )
       sglist
       )
  )

(defn create-sg-list-full
  "Creates a minion list without names for each service group"
  []
  {:post [(s/valid? ::ss/serviceGroups %)]
   }
  (map (fn [{:keys [sg_id sg_name] :as sg}]
         (assoc sg :minions (create-minion-list-for-sg sg_id))
         )
       (sql/get-sgs)
       )
  )

(defn print-new-sg-list
  [fname]
  "Creates a minion list and prints it to fname"
  (->> (create-sg-list-full)
       minion-list-to-csv
       (interpose \newline)
       (apply str)
       (spit fname)
       )
  )
