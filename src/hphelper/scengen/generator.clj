(ns hphelper.scengen.generator
  (:require [clojure.core.typed :as t]
            [hphelper.shared.sql :as sql]
            [hiccup.core :refer :all]
            [hphelper.chargen.generator :as cg]
            [clojure.tools.logging :as log]
            )
  (:gen-class)
  )

(def sectorIndicies
  "The four sector indicies: Happiness, Compliance, Loyalty, and Security"
  [:HI :CI :LI :SI])

(defn- create-base-indicies-list
  "Combines the base indicies with sg indicies, and sets them all to 0"
  []
  (merge
   (reduce merge (map (fn [k] {k 0}) sectorIndicies))
   (reduce merge (map (fn [k] {k 0}) (map (comp keyword :sg_abbr)
                                          (sql/query "SELECT sg_abbr FROM sg"))))))

(defn- normalise-specific-indicies
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

(defn- fuzzify-indicies
  "Randomly adds, subtracts, or leaves alone each of the indicies"
  [indicies]
  ((apply comp (map (fn [kw] (fn [ind] (update-in ind [kw] + (dec (rand-int 3)))))
                    (keys indicies))
          )
   indicies))

(defn- adjust-index
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

(defn- get-crisis-id-list
  "Taking a full crisis record, returns a collection of the crisis ids"
  [{crisises :crisises :as crisRec}]
  (map :c_id crisises))

(defn- select-crisis-tags
  "Selects all the crisis tags for a single crisis"
  [crisisNum]
  (map :ct_tag (sql/query "SELECT * FROM crisis_tag WHERE c_id = ?;" crisisNum)))

(defn- normalise-all-indicies
  "Normalises the sector indicies, and the service group indicies"
  [indicies]
  (->> indicies
      (normalise-specific-indicies sectorIndicies)
      (normalise-specific-indicies (map key (remove (partial some (into #{} sectorIndicies)) indicies)))))

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

(defn- create-single-minion-list
  "Given a service group record, creates a minion list under a :minions keyword"
  ([{sgNum :sg_id :as sgRec}]
   (assoc-in sgRec [:minions]
             (remove nil? (set (map (fn [skill] (sql/get-random-item (sql/query "SELECT `minion`.* FROM `minion`, `minion_skill`
                                                               WHERE `sg_id` = ?
                                                               AND `minion`.`minion_id` IN
                                                               (SELECT `minion_id`
                                                               FROM `minion_skill`
                                                               WHERE `skills_id` = ?);"
                                                               sgNum
                                                               skill))) ;; Grabs a minion with the skill from the service group
                       (map :skills_id (sql/query "SELECT `skills_id`
                                                  FROM `sg_skill`
                                                  WHERE `sg_id` = ?;"
                                                  sgNum))))))))

(defn- add-additional-minions
  "Given a service group record, adds random minions till minimum minions have been reached,
  or till tries remaining is 0"
  ([minMinions triesRemaining {sgNum :sg_id minions :minions :as sgRec}]
   (if (and (< (count minions) minMinions)
            (> triesRemaining 0))
     (recur
       minMinions
       (dec triesRemaining)
       (assoc-in sgRec [:minions]
                 (into #{} (conj minions
                                 (sql/get-random-item (sql/query "SELECT * FROM `minion` WHERE `sg_id` = ?;"
                                                                 sgNum))))))
       sgRec)))

(defn- sort-minion-list
  "Given a minion record, converts minions to a vector then sorts it by cost"
  ([{minions :minions :as sgRec}]
   (assoc-in sgRec [:minions]
             (sort-by :minion_name (into [] minions)))))

(defn- select-minion-lists
  "Creates minion lists for each service group under :minions"
  ([scenRec]
   (assoc-in scenRec [:minions]
             (map (comp sort-minion-list
                        (partial add-additional-minions 12 12) ;; Want at least 10 minions on each list
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

(defn- create-random-zone-name
  "Creates a randome 3 letter zone name"
  []
  (apply str (take 3 (repeatedly (fn [] (char (+ (int \A) (rand-int 26))))))))

(defn- check-zone-name
  "Checks for zone name, and if missing creates a random sector name"
  [{zone :zone :as scenRec}]
  (if (and zone
           (string? zone)
           (= (count zone) 3))
    scenRec
    (assoc-in scenRec [:zone] (apply str (repeatedly 3 (fn [] (char (+ (rand-int 26) (int \A)))))))))

(defn add-character
  "Adds a high programmer character to the record under :hps"
  ([{hps :hps :as scenRec} {nam :name :as character}]
   (assert nam "Character is unnamed and unfinished")
   (assoc-in scenRec [:hps]
             (concat '() hps (list character)))))

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
       )))

(:zone (create-scenario))
