(ns hphelper.shared.sql
  (:require [clojure.java.jdbc :as jdb]
            [clojure.tools.logging :as log]
            ))

(def db {:subprotocol "mysql"
         :subname "//127.0.0.1:3306/hphelper"
         :user "fc"})


(defn get-random-item
  "Gets a random item from a collection. Returns nil if the collection is empty"
  ([collection]
   (if (= (count collection) 0)
     nil
     (nth collection (int (Math/floor (* (Math/random) (count collection))))))))

(defn query
  "Performs the query against the defined database, and logs the query"
  [& quer]
  (log/trace "Performing Query: " quer)
  (jdb/query db quer))

(defn get-sg-by-id
  "Gets service group by sg_id"
  [sgId]
  (assert (integer? sgId))
  (:sg_name (first (query "SELECT sg_name FROM sg WHERE sg_id = ?;" sgId))))

(defn get-ss-by-id
  "Gets service group by sg_id"
  [ssId]
  (:ss_name (first (query "SELECT ss_name FROM ss WHERE ss_id = ?;" ssId))))

(defn get-result
  "Shorthand to return the first value of a single-result query"
  [result]
  (-> result (first) (first) (val)))

(defn get-random-row
  "Gets a random row from a table, returns a single map"
  [table idField]
  (let [fCount (get-result (query (str "SELECT COUNT(" idField ") FROM " table ";")))]
    (first (query (str "SELECT * FROM " table " WHERE " idField " = ?;")
                       (int (Math/ceil (* (Math/random)
                                          fCount)))))))

(defn get-random-from-table
  "Gets a random value from a field in a table"
  [table field idField]
  (let [fCount (get-result (query (str "SELECT COUNT(" idField ") FROM " table ";")))]
    (get-result (query (str "SELECT " field " FROM " table " WHERE " idField " = ?;")
                       (int (Math/ceil (* (Math/random)
                                          fCount)))))))


(defn get-random-society
  "Gets a random secret society from the database. Returns a map."
  []
  (get-random-row "ss_skills" "ss_id"))

(defn get-society
  "Gets a secret society by id"
  [ssId]
  (first (query "SELECT * FROM ss_skills WHERE ss_id = ?;" ssId)))

(defn get-random-drawback
  "Gets a random drawback from the database. Returns a string."
  []
  (get-random-from-table "drawbacks" "text" "id"))

(defn get-random-mutation
  "Gets a random mutation from the database. Returns a map"
  []
  (get-random-row "mutations" "id"))

(defn get-news-crisis
  "Gets the news articles from a crisis, returns a vector"
  [crisisId]
  (into [] (map :news_desc (query "SELECT * FROM news WHERE c_id = ?;" crisisId))))

(defn get-news-random-single
  "Gets a random unassociated news item"
  []
  (:news_desc (rand-nth (query "SELECT * FROM news WHERE c_id IS NULL;"))))

(defn get-news-random
  "Gets up to numb news items unassociated with crisises. Returns a vector."
  ([numb]
   (get-news-random numb #{}))
  ([numb items]
   (if (<= numb 0)
     (into [] items) ;; Is done, return a vector
     (recur (dec numb) (conj items (get-news-random-single))))))

(defn get-random-name
  "Gets a random name from the database.
  If supplied with a collision set, tries up to five times to return a name not in that set."
  ([]
   (let [nameMap (get-random-item (query "SELECT * FROM `name` WHERE `name_clearance` = 'U';"))]
     (str (nameMap :name_first) "-"
          (nameMap :name_clearance) "-"
          (nameMap :name_zone))))
  ([collisionSet]
   (get-random-name collisionSet 5))
  ([collisionSet triesRemaining]
   (let [tempName (get-random-name)]
     (if (and (some #{tempName} collisionSet)
              (> triesRemaining 0))
       (recur collisionSet (dec triesRemaining))
       tempName)))
  )
