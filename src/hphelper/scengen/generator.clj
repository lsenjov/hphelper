(ns hphelper.scengen.generator
  (:require [hphelper.shared.sql :as sql]
            [hiccup.core :refer :all]
            [hphelper.chargen.generator :as cg]
            [taoensso.timbre :as log]
            [hphelper.shared.indicies :refer :all]
            [clojure.spec :as s]
            )
  (:gen-class)
  )

(defn- get-crisis-id-list
  "Taking a full crisis record, returns a collection of the crisis ids"
  [{crisises :crisises :as crisRec}]
  (map :c_id crisises))

(defn- select-crisis-tags
  "Selects all the crisis tags for a single crisis"
  [crisisNum]
  (map :ct_tag (sql/query "SELECT * FROM crisis_tag WHERE c_id = ?;" crisisNum)))

(defn- load-crisis-indicies
  "Updates the indicies list modified by crisis tags and normalised
  Requries crisies and base indicies to be initialised"
  [scenRec]
  (update-in scenRec [:indicies]
              (apply comp (map partial
                               (repeat adjust-index)
                               (apply concat (map select-crisis-tags (get-crisis-id-list scenRec)))))
              ))

(defn- add-crisis-desc
  "Given a crisis map, adds an :extraDesc field with a vector of string descriptors"
  [zone {id :c_id :as crisisMap}]
  (assoc-in crisisMap [:extraDesc]
            (sql/get-crisis-desc zone id)))

(defn- add-crisis-descriptions
  "Given crisises, adds extra description to each"
  [{crisises :crisises zone :zone :as crisRec}]
  (assert crisises)
  (assoc-in crisRec [:crisises]
            (map (partial add-crisis-desc zone)
                 crisises)))

(defn- select-crisises
  "If given a vector of crisis id integers, adds full crisis maps to the record, then fills remaining spots up to 3 crisises.
  If the :crisises key is empty, adds the key with three randomly selected crisises"
  [{crisises :crisises zone :zone :as crisRec}]
  (if (nil? crisises)
    (recur (assoc-in crisRec [:crisises] #{}))
    (if (vector? crisises)
      (recur (assoc-in crisRec [:crisises]
                       (into #{} (remove nil? (set (map (partial sql/get-crisis-by-id zone)
                                                        crisises))))))
      (if (< (count crisises) 3)
        (recur (assoc-in crisRec [:crisises]
                         (into #{} (remove nil? (conj crisises (sql/get-random-crisis zone))))))
        crisRec
        ))))

(defn- select-service-group-directives
  "Adds service group directives to scenario record associated with the current queries"
  ([{crisises :crisises zone :zone :as crisRec}]
   (assoc-in crisRec [:directives]
             (apply concat (map (partial sql/get-directive-crisis zone)
                                (get-crisis-id-list crisRec))))))

(defn- select-service-group-directives-unused
  "Adds directives to service groups without one"
  ([{directives :directives zone :zone :as crisRec}]
   (log/trace "Directives before unused" directives)
   (assoc-in crisRec [:directives]
             (concat directives
                     (map (partial sql/get-directive-unused zone)
                          (remove (set (map :sg_id directives)) ;; Removing used sgs from all sgs
                                  (map :sg_id
                                       (sql/query "SELECT `sg_id` FROM `sg`;"))))))))

(defn- select-secret-society-missions
  "Adds secret society directives to scenario record associated with the current crisises"
  ([{crisises :crisises zone :zone :as crisRec}]
   (assoc-in crisRec [:societies]
             (apply concat (map (partial sql/get-secret-society-missions zone)
                                (get-crisis-id-list crisRec))))))

(defn- select-secret-society-missions-unused
  "Adds directives to service groups without one"
  ([{societies :societies zone :zone :as crisRec}]
   (assert societies)
   (assoc-in crisRec [:societies]
             (concat societies
                     (map (partial sql/get-secret-socity-mission-unused zone)
                          (remove (set (map :ss_id societies)) ;; Removing used sss from all sgs
                                  (map :ss_id
                                       (sql/query "SELECT `ss_id` FROM `ss`;"))))))))

(defn- add-minion-unique
  "Adds a minion to a list of minions only if the minion name and clearance are unique"
  [l m]
  (if (some (fn [{mn1 :minion_name mc1 :minion_clearance}
                 ]
              (and (= mn1 (:minion_name m)) (= mc1 (:minion_clearance m)))
              )
            l
            )
    l
    (conj l m)
    )
  )


(defn- cull-duplicate-minions
  "Given a sg record, makes sure the minions are unique with regards to name and clearance"
  ([sg]
   (cull-duplicate-minions '() (:minions sg) sg))
  ;; In is a list of incoming, out is a list of minions to return, sg is the original list
  ([out in sg]
   (if (= 0 (count in))
     (assoc sg :minions out)
     (recur (add-minion-unique out (first in)) (rest in) sg)
     )
   )
  )

(defn- create-single-minion-list
  "Given a service group record, creates a minion list under a :minions keyword"
  ([{sgNum :sg_id :as sgRec}]
     ;; Get a list of the specialty skills of that service group
     (->> (sql/query "SELECT `skills_id` FROM `sg_skill` WHERE `sg_id` = ?;"
                     sgNum)
          ;; Get a list of the ids
          (map :skills_id)
          ;; Get minions
          (map sql/get-single-minion-from-sg-and-skill (repeat sgNum))
          ;; Removes possible results from there not being a minion with that skill
          (remove nil?)
          ;; Put it back in the service group
          (assoc sgRec :minions)
          ;; Cull duplicates
          (cull-duplicate-minions)
     )
   )
  )

(defn- add-additional-minions
  "Given a service group record, adds random minions till minimum minions have been reached,
  or till tries remaining is 0"
  ([minMinions triesRemaining {sgNum :sg_id minions :minions :as sgRec}]
   (if (and (< (count minions) minMinions)
            (> triesRemaining 0))
     (recur
       minMinions
       (dec triesRemaining)
       (update-in sgRec
                  [:minions]
                  add-minion-unique
                  (sql/get-single-minion-from-sg sgNum)
                  )
       )
     sgRec)))

(defn- sort-minion-list
  "Given a minion record, converts minions to a vector then sorts it by cost"
  ([{minions :minions :as sgRec}]
   (assoc-in sgRec [:minions]
             (sort-by :minion_name (into [] minions)))))

(defn- select-minion-lists
  "Creates minion lists for each service group under :serviceGroups"
  ([scenRec]
   (assoc-in scenRec [:serviceGroups]
             (map (comp sort-minion-list
                        (partial add-additional-minions 14 14) ;; Want at least 14 minions on each list
                        (partial create-single-minion-list))
                  (sql/query "SELECT * FROM `sg`;")))))

(defn- select-news
  "Selects crisis-related news and other news articles"
  ([{zone :zone :as scenRec}]
   (assoc-in scenRec [:news]
             (shuffle (concat (apply concat
                                     (map (partial sql/get-news-crisis zone)
                                          (get-crisis-id-list scenRec)))
                              (sql/get-news-random zone 6))))))

(defn- check-zone-name
  "Checks for zone name, and if missing creates a random sector name"
  [{zone :zone :as scenRec}]
  (if (and zone
           (string? zone)
           (> (count zone) 0))
    scenRec
    (assoc-in scenRec [:zone] (sql/create-random-zone-name))))

(defn- minutes-to-readable
  "Changes an integer minutes to a readable 1h14m format"
  [t]
  (str (int (Math/floor (/ t 60)))
       "h"
       (int (mod t 60))
       "m"))

(defn- generate-times
  "Returns a vector of times in minutes, distributed between start and finish"
  ([start finish n]
   (map (comp minutes-to-readable
              int
              (partial + start)
              #(+ (rand-int 11) %) ;; Adds anywhere between 0 and 10 minutes, to fuzzify times a little
              (partial * (/ (- finish start) n)))
        (range n))))

(defn- generate-cbay
  "Generates cbay articles from both crisises and random items"
  [{cbay :cbay :as scenRec}] ;; Cbay may or may not exist
  (assoc-in scenRec [:cbay]
            (into []
                  (let [items (concat (sql/get-random-cbay-items (scenRec :zone) 5)
                                      cbay)] ;; If cbay does not exist, will be as before.
                    (map str
                         items
                         (repeat " ")
                         (generate-times 15 150 (count items)))))))

(defn add-character
  "Adds a high programmer character to the record under :hps"
  ([{hps :hps :as scenRec} {nam :name :as character}]
   (assert nam "Character is unnamed and unfinished")
   (assoc-in scenRec [:hps]
             (concat '() hps (list character)))))

(defn- copy-key
  "Associates the key from source to the target"
  [source k dest]
  (assoc dest k (get source k)))

(defn purge-unused
  "Removes unused keys that are generally holdovers from elsewhere"
  [scenRec]
  ((apply comp
          (map partial
               (repeat copy-key)
               (repeat scenRec)
               [:zone :crisises :directives :societies :serviceGroups :news :indicies :hps :cbay]))
   {}
   ))

(defn- get-single-crisis-keywords
  "Returns a list of all keywords associated with this crisis"
  [^Integer cid]
  {:post [(s/valid? (s/coll-of string?) %)]}
  (let [ending (str "-" cid)
        nameKeys (set (filter #(clojure.string/ends-with? % ending)
                              (keys @sql/namedItems)))
        ]
    (->> @sql/namedItems
         (filter (fn [[k v]] (nameKeys k)))
         (map second)
         )
    )
  )
(defn- get-random-keywords
  "Returns n random keywords from all keywords"
  [^Integer n]
  {:post [(s/valid? (s/coll-of string?) %)]}
  (->> @sql/namedItems
       (vals)
       (shuffle)
       (take n)
       )
  )
(defn fuzz-number-by-one
  "Randomly changes a single integer by one, either up, down, or leave"
  [^Integer n]
  (-> n
       (- 1)
       (+ (rand-int 2))
       )
  )
(defn- create-random-keywords
  "Returns n randomly created keywords of each CIT, RES, and LOC"
  [^String zone ^Integer n]
  {:post [(s/valid? (s/coll-of string?) %)]}
  (map (comp (partial sql/interpret-line zone)
             (partial sql/interpret-token zone))
       (concat (for [c (take (fuzz-number-by-one n) (shuffle sql/allClearances))]
                 (str "CIT-" c "-"))
               (take (fuzz-number-by-one n) (repeatedly #(str "RES-" (rand-int 100) "-")))
               (take (fuzz-number-by-one n) (repeatedly #(str "LOC-" (rand-int 100) "-")))
               (take (fuzz-number-by-one n) (repeatedly #(str "SUB-" (rand-int 100) "-")))
               )
       )
  )
(defn- assoc-crisis-keywords
  "Gets all name keywords ending with '-num' from names"
  [{:keys [crisises zone] :as game}]
  (assoc game
         :keywords
         (->> (mapcat get-single-crisis-keywords
                         (conj (map :c_id crisises) 0)
                         )
              (map (partial sql/interpret-line zone))
              (concat (create-random-keywords zone 2))
              (shuffle)
              )
         )
  )

(defn create-scenario
  "Creates a generated scenario"
  ([] (create-scenario {}))
  ([scen]
   (-> scen
       (check-zone-name)
       (select-crisises)
       (add-crisis-descriptions)
       (select-service-group-directives)
       (select-service-group-directives-unused)
       (select-secret-society-missions)
       (select-secret-society-missions-unused)
       (select-minion-lists)

       (select-news)

       ;; Creating Indicies
       (assoc-in [:indicies] (create-base-indicies-list))
       (load-crisis-indicies)
       (update-in [:indicies] fuzzify-indicies)
       (update-in [:indicies] fuzzify-indicies)
       (update-in [:indicies] normalise-all-indicies)
       (generate-cbay)
       ;(add-character (cg/create-character))
       (purge-unused)
       (assoc-crisis-keywords)
       )))
