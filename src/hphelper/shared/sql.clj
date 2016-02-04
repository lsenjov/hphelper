(ns hphelper.shared.sql
  (:require [clojure.java.jdbc :as jdb]
            [clojure.tools.logging :as log]
            ))

(def db {:subprotocol "mysql"
         :subname "//127.0.0.1:3306/hphelper"
         :user "fc"})

(defn query
  "Performs the query against the defined database, and logs the query"
  [& quer]
  (log/trace "Performing Query: " quer)
  (jdb/query db quer))

(def namedItems
  "A map atom with references as keys and named items as values"
  (atom {}))

(defn reset-named-items!
  "Resets the namedItems atom"
  []
  (reset! namedItems {}))

(defn get-name
  "Returns the referenced name from namedItems, or nil if not existing"
  [keyName]
  (get @namedItems keyName))

(defn named-exists?
  "Returns true if the named item already exists in namedItems, else false"
  [refName]
  (if (some #{refName} (vals @namedItems))
    true
    false))

(defn add-name
  "Adds the reference name pair to namedItems, overwriting any previous allocation.
  Returns the added name"
  ([keyRef valName]
   (swap! namedItems assoc keyRef valName)
   valName)
  ([[keyRef valName]]
   (add-name keyRef valName)))

(defn- create-name
  "Creates the name and checks for collisions. After tries runs out just returns the current guess.
  Adds the name to namedItems and returns the new name."
  [nameGen nameRef triesRemaining]
  (let [currentTry (nameGen)]
    (if (and (named-exists? currentTry)
             (> triesRemaining 0))
      (recur nameGen nameRef (dec triesRemaining))
      (add-name nameRef currentTry))))

(defn get-or-create-name
  "Takes a name generation function and a reference, and tries multiple times to create a non-colliding name
  for the reference. If the reference already exists, returns the associated name. After multiple collision attempts
  will simply add the latest try to the map."
  ([nameGen nameRef]
   (if (get-name nameRef)
     (get-name nameRef) ;; It exists, return the name
     (create-name nameGen nameRef 10)))) ;; Otherwise, let's try and make this work

(defn get-random-item
  "Gets a random item from a collection. Returns nil if the collection is empty"
  ([collection]
   (if (= (count collection) 0)
     nil
     (nth collection (int (Math/floor (* (Math/random) (count collection))))))))

(defn get-random-name
  "Gets a random name from the database. at a specified clearance level"
  ([clearance]
   (let [nameMap (get-random-item (query "SELECT * FROM `name` WHERE `name_clearance` = ?;" clearance))]
     (str (nameMap :name_first) "-"
          (nameMap :name_clearance) "-"
          (nameMap :name_zone))))
  )

(def allClearances
  "All possible clearances for citizens"
  ["IR" "R" "O" "Y" "G" "B" "I" "V" "U"])

(defn- interpret-citizen-name
  "Interprets a string in form ##CIT-V-TAGS##, where the second part is the clearance level"
  [token]
  (let [tokenised (clojure.string/split token #"-")]
    (if (or (<= (count tokenised) 1) ;; If not enough parts
            (not (some #{(second tokenised)} allClearances))) ;; or if second part is not one of the clearances
      (do ;; Fail and return token
        (log/error "Citizen name token in incorrect form:" token "second token is" (second tokenised))
        token)
      (get-or-create-name (partial get-random-name (second tokenised)) token))))

(defn- interpret-token
  "Takes a string of format ABC-TAGS-AND-OTHERS and looks at the first part, choosing which
  function to call, then calls get-or-create-name. On an error logs and returns the token."
  [zoneName token]
  (let [firstPart (first (clojure.string/split token #"-"))]
    (case firstPart
      "ZON" zoneName
      "CIT" (interpret-citizen-name token)
      (do
        (log/error "Incorrect token form:" token)
        token)
      )
    ))

(defn interpret-line
  "Takes a string, and looks for ##ABC-TAGS##, replacing these tokens with the correct items.
  Also takes a zone name and optional crisisId. Returns the new line, or the old line if it has an error"
  ([zoneName line]
   (interpret-line zoneName line ""))
  ([zoneName line crisisId]
   (assert (string? line) "Line to look at is not a string!")
   (let [tokenised (clojure.string/split line #"##" 3)]
     (case (count tokenised)
       1 line
       2 (do (log/error "Incorrect line in crisis" crisisId " line is:" line)
             line)
       3 (recur zoneName
                (str (first tokenised)
                     (interpret-token zoneName (str (second tokenised) "-" crisisId))
                     (nth tokenised 2))
                crisisId
       ))))
  )

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
  [zone crisisId]
  (into [] (map (fn [rec] (interpret-line zone
                                          (rec :news_desc)
                                          (rec :c_id)))
                (query "SELECT * FROM news WHERE c_id = ?;" crisisId))))

(defn get-news-random-single
  "Gets a random unassociated news item"
  [zone]
  (interpret-line zone (:news_desc (rand-nth (query "SELECT * FROM news WHERE c_id IS NULL;")))))

(defn get-news-random
  "Gets up to numb news items unassociated with crisises. Returns a vector."
  ([zone numb]
   (get-news-random zone numb #{}))
  ([zone numb items]
   (if (<= numb 0)
     (into [] items) ;; Is done, return a vector
     (recur zone (dec numb) (conj items (get-news-random-single zone))))))

