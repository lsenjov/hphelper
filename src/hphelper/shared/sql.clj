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

;; Until reset, this will save all references. The same number crisis will
;; continue to return the same names for ##REF-TAGS##
;; It is done this way so names aren't overwritten during frequent updating
;; (resaving source files will re-load them into memory)
(defn init-named-items
  "Checks if namedItems is initialised. If not, initialise it"
  []
  (if (not (resolve 'namedItems))
    (def namedItems
      "A map atom with references as keys and named items as values"
      (atom {}))))

(init-named-items)

(defn reset-named-items!
  "Resets the namedItems atom"
  []
  (reset! namedItems {}))

(defn get-name
  "Returns the referenced name from namedItems, or nil if not existing"
  [keyName]
  (init-named-items)
  (get @namedItems keyName))

(defn named-exists?
  "Returns true if the named item already exists in namedItems, else false"
  [refName]
  (init-named-items)
  (if (some #{refName} (vals @namedItems))
    true
    false))

(defn add-name
  "Adds the reference name pair to namedItems, overwriting any previous allocation.
  Returns the added name"
  ([keyRef valName]
   (init-named-items)
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

(def allClearances
  "All possible clearances for citizens"
  ["IR" "R" "O" "Y" "G" "B" "I" "V" "U"])

(defn create-random-zone-name
  "Creates a random three letter zone name"
  []
  (apply str (repeatedly 3 (fn [] (char (+ (rand-int 26) (int \A)))))))

(defn get-random-name
  "Gets a random name from the database. at a specified clearance level"
  ([clearance]
   (let [nameMap (first (query "SELECT * FROM `first_name` LIMIT 1 OFFSET ?;"
                                         ((comp rand-int :cnt first)
                                          (query "SELECT COUNT(`fn_id`) AS `cnt` FROM `first_name`"))))]
     (str (nameMap :fn_name) "-"
          clearance "-"
          (create-random-zone-name))))
  )

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

(defn- get-random-resource
  "Gets a random resource from the database"
  [resType]
  (:resource_name
   (rand-nth
    (query "SELECT resource_name FROM resource WHERE resource_type LIKE ?;" resType))))

(defn- interpret-token
  "Takes a string of format ABC-TAGS-AND-OTHERS and looks at the first part, choosing which
  function to call, then calls get-or-create-name. On an error logs and returns the token."
  [zoneName token]
  (let [firstPart (first (clojure.string/split token #"-"))]
    (case firstPart
      "ZON" zoneName
      "CIT" (interpret-citizen-name token)
      "LOC" (get-or-create-name (partial get-random-resource "LOC") token)
      "RES" (get-or-create-name (partial get-random-resource "RES") token)
      (do
        (log/error "Incorrect token form:" token)
        token)
      )
    ))

(defn interpret-line
  "Takes a string, and looks for ##ABC-TAGS##, replacing these tokens with the correct items.
  Also takes a zone name and optional crisisId. Returns the new line, or the old line if it has an error"
  ([zoneName line]
   (interpret-line zoneName 0 line))
  ([zoneName crisisId line]
   (assert (string? line) (str "Line to look at is not a string! Is actually:" line))
   (let [tokenised (clojure.string/split line #"##" 3)]
     (case (count tokenised)
       1 line
       2 (do (log/error "Incorrect line in crisis" crisisId " line is:" line)
             line)
       3 (recur zoneName
                crisisId
                (str (first tokenised)
                     (interpret-token zoneName (str (second tokenised) "-" crisisId))
                     (nth tokenised 2))
       )))))

;; Naming done, now onto the rest of it
(defn get-crisis-by-id
  "Gets a specific crisis by its id, returns a map, or nil if the crisis does not exist"
  [zone crisisId]
  (let [crisis (first (query "SELECT * FROM `crisis` WHERE `c_id` = ?;" crisisId))]
    (if (not crisis)
      nil
      (update-in crisis [:c_desc]
                 (partial interpret-line zone crisisId)))))

(defn get-random-crisis
  "Gets a random crisis, returning a map"
  [zone]
  (let [crisis (rand-nth (query "SELECT * FROM `crisis`;"))]
  (update-in crisis
             [:c_desc]
             (partial interpret-line zone (crisis :c_id)))))

(defn get-crisis-desc
  "Gets the descriptors from a single crisis, interprets, and returns a vector of strings"
  [zone crisisId]
  (into [] (map (comp (partial interpret-line zone crisisId) :ct_desc)
                (query "SELECT `ct_desc` FROM `crisis_text` WHERE `c_id` = ?;" crisisId))))

(defn get-secret-society-missions
  "Selects all the secret society missions of a single crisis"
  [zone crisisId]
  (map update-in
       (query "SELECT * FROM `ssm` WHERE `c_id` = ?;" crisisId)
       (repeat [:ssm_text])
       (repeat (partial interpret-line zone crisisId))))

(defn get-secret-socity-mission-unused
  "Selects a single secret society mission not associated to a crisis"
  [zone ssId]
  (update-in (get-random-item (query "SELECT * FROM `ssm` WHERE `ss_id` = ? AND `c_id` IS NULL;" ssId))
             [:ssm_text]
             (partial interpret-line zone)))

(defn get-directive-crisis
  "Selects directives related to a single crisis"
  [zone crisisId]
  (map update-in
       (query "SELECT * FROM `sgm` WHERE `c_id` = ?;" crisisId)
       (repeat [:sgm_text])
       (repeat (partial interpret-line zone crisisId))))

(defn get-directive-unused
  "Selects a single service group directive not associated to a crisis"
  [zone sgId]
  (update-in (get-random-item (query "SELECT * FROM `sgm` WHERE `sg_id` = ? AND `c_id` IS NULL;" sgId))
             [:sgm_text]
             (partial interpret-line zone)))

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
                                          (rec :c_id)
                                          (rec :news_desc)))
                (query "SELECT * FROM news WHERE c_id = ?;" crisisId))))

(defn get-news-random-single
  "Gets a random unassociated news item"
  [zone]
  (interpret-line zone (:news_desc (rand-nth (query "SELECT * FROM news WHERE c_id IS NULL;")))))

(defn- cbay-format-single
  "Formats a single cbay record to a readable string"
  [item]
  (str (item :cbay_item) " " (item :cbay_cost) " ACCESS"))

(defn  get-cbay-random-single
  "Gets a random unassociated cbay item"
  [zone]
  (interpret-line zone
                  (let [item (rand-nth (query "SELECT * FROM cbay WHERE c_id IS NULL;"))]
                    (cbay-format-single item))))

(defn get-news-random
  "Gets up to numb news items unassociated with crisises. Returns a vector."
  ([zone numb]
   (get-news-random zone numb #{}))
  ([zone numb items]
   (if (<= numb 0)
     (into [] items) ;; Is done, return a vector
     (recur zone (dec numb) (conj items (get-news-random-single zone))))))

(defn get-random-cbay-items
  "Gets up to numb cbay items unassociated with crisises generated by f. Returns a vector."
  ([zone numb]
   (shuffle
     (vec (set ;; The vec/set shuffle removes duplicates
         (repeatedly numb (partial get-cbay-random-single zone)))))))

