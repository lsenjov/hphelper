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

(defn create-base-indicies-list
  "Combines the base indicies with sg indicies, and sets them all to 0"
  []
  (merge
   (reduce merge (map (fn [k] {k 0}) sectorIndicies))
   (reduce merge (map (fn [k] {k 0}) (map (comp keyword :sg_abbr)
                                          (sql/query "SELECT sg_abbr FROM sg"))))))

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
  "If given a vector of crisis id integers, adds full crisis maps to the record, then fills remaining spots up to 3 crisises.
  If the :crisises key is empty, adds the key with three randomly selected crisises"
  [{crisises :crisises :as crisRec}]
  (if (nil? crisises)
    (recur (assoc-in crisRec [:crisises] #{}))
    (if (vector? crisises)
      (recur (assoc-in crisRec [:crisises]
                       (into #{} (remove nil? (set (map (fn [id] (first (sql/query "SELECT * FROM `crisis` WHERE `c_id` = ?;" id)))
                                              crisises))))))
      (if (< (count crisises) 3)
        (recur (assoc-in crisRec [:crisises]
                         (into #{} (remove nil? (conj crisises (sql/get-random-row "crisis" "c_id"))))))
        crisRec
        ))))

(defn- select-service-group-directives
  "Adds service group directives to scenario record associated with the current queries"
  ([{crisises :crisises :as crisRec}]
   (assoc-in crisRec [:directives]
             (apply concat (map (fn [crisNum] (sql/query "SELECT * FROM `sgm` WHERE `c_id` = ?;" crisNum))
                                (get-crisis-id-list crisRec))))))

(defn- select-service-group-directives-unused
  "Adds directives to service groups without one"
  ([{directives :directives :as crisRec}]
   (log/trace "Directives before unused" directives)
   (assoc-in crisRec [:directives]
             (concat directives
                     (map (comp sql/get-random-item
                                (fn [sgNum]
                                  (sql/query "SELECT * FROM `sgm` WHERE `sg_id` = ? AND `c_id` IS NULL;" sgNum)))
                          (remove (set (map :sg_id directives)) ;; Removing used sgs from all sgs
                                  (map :sg_id
                                       (sql/query "SELECT `sg_id` FROM `sg`;"))))))))

(defn- select-secret-society-missions
  "Adds secret society directives to scenario record associated with the current crisises"
  ([{crisises :crisises :as crisRec}]
   (assoc-in crisRec [:societies]
             (apply concat (map (fn [crisNum] (sql/query "SELECT * FROM `ssm` WHERE `c_id` = ?;" crisNum))
                                (get-crisis-id-list crisRec))))))

(defn- select-secret-society-missions-unused
  "Adds directives to service groups without one"
  ([{societies :societies :as crisRec}]
   (assert societies)
   (assoc-in crisRec [:societies]
             (concat societies
                     (map (comp rand-nth
                                (fn [ssNum]
                                  (sql/query "SELECT * FROM `ssm` WHERE `ss_id` = ? AND `c_id` IS NULL;" ssNum)))
                          (remove (set (map :ss_id societies)) ;; Removing used sss from all sgs
                                  (map :ss_id
                                       (sql/query "SELECT `ss_id` FROM `ss`;"))))))))

(defn- create-single-minion-list
  "Given a service group record, creates a minion list under a :minions keyword"
  ([{sgNum :sg_id :as sgRec}]
   (assoc-in sgRec [:minions]
             (set (map (fn [skill] (sql/get-random-item (sql/query "SELECT `minion`.* FROM `minion`, `minion_skill`
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
                                                  sgNum)))))))

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
                 (conj minions
                       (sql/get-random-item (sql/query "SELECT * FROM `minion` WHERE `sg_id` = ?;"
                                                   sgNum)))))
     sgRec)))

(defn- sort-minion-list
  "Given a minion record, converts minions to a vector then sorts it by cost"
  ([{minions :minions :as sgRec}]
   (assoc-in sgRec [:minions]
             (sort-by :minion_name (into [] minions)))))

(defn- select-minion-lists
  ([scenRec]
   (assoc-in scenRec [:minions]
             (map (comp sort-minion-list
                        (partial add-additional-minions 10 10) ;; Want at least 10 minions on each list
                        (partial create-single-minion-list))
                  (sql/query "SELECT * FROM `sg`;")))))

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
       (select-crisises)
       (add-crisis-descriptions)
       (select-service-group-directives)
       (select-service-group-directives-unused)
       (select-secret-society-missions)
       (select-secret-society-missions-unused)
       (select-minion-lists)
       )))

(keys (create-scenario))
