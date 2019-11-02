(ns hphelper.server.shared.saveload
  (:require [clojure.java.jdbc :as jdb]
            [clojure.edn :as edn]
            [taoensso.timbre :as log]
            ))

(def db
  (-> "mysql.edn"
      slurp
      clojure.edn/read-string 
      (update-in [:subname] str "hpsaveload")
      ))

;; Scenarios
(defn save-scen-to-db
  "Takes a data object, saves it as an edn string in the database.
  Returns the generated key"
  [obj]
  ;; Don't delete this line. For some strange reason, deleting this line or removing obj puts debug data into the saved file
  ;; Might have something to do with lazy evaluation...somehow. Printing the obj evaluates it properly
  (log/trace "save-scen-to-db. obj:" obj)
  (:generated_key (first (jdb/insert! db :scen {:scen_file (prn-str obj)}))))
(defn load-scen-from-db
  "Takes an integer key, gets the data object from the database."
  [^Integer k]
  (log/trace "load-scen-from-db. Scenario number:" k)
  (let [in (:scen_file (first (jdb/query db ["SELECT scen_file FROM scen WHERE scen_id = ?;" k])))]
    (log/trace "Loaded scenario string:" in)
    (try (edn/read-string in)
         (catch Exception e
           (do (log/error "Could not load scen. Dumped to debug.txt")
               (spit "debug.txt" in)
               (throw e)
               )
           )
         )
    )
  )
(defn get-scen-ids
  "Returns a list of all scenario ids"
  []
  (map :scen_id (jdb/query db ["SELECT scen_id FROM scen"])))

;; Scenarios With Characters
(defn save-fullscen-to-db
  "Takes a data object, saves it as an edn string in the database.
  Returns the generated key"
  [obj]
  (assert (:directives obj) "No directives exist?")
  (:generated_key (first (jdb/insert! db :fullscen {:fs_file (prn-str obj)}))))
(defn load-fullscen-from-db
  "Takes an integer key, gets the data object from the database."
  [^Integer k]
  (let [in (:fs_file (first (jdb/query db ["SELECT fs_file FROM fullscen WHERE fs_id = ?;" k])))]
    (try (edn/read-string in)
         (catch Exception e
           (do (log/error "Could not load scenario. Dumped to debug.txt")
               (spit "debug.txt" in)
               (throw e)
               )
           )
         )
    )
  )
(defn get-fullscen-ids
  "Returns a list of all scenario ids"
  []
  (map :fs_id (jdb/query db ["SELECT fs_id FROM fullscen"])))

;; Characters
(defn save-char-to-db
  "Takes a data object, saves it as an edn string in the database.
  Returns the generated key"
  [obj]
  (log/trace "save-char-to-db.")
  (let [
        char_file {:char_file (prn-str obj) :char_name (:name obj)}
        _ (log/trace "char_file" char_file)
        transaction (jdb/insert! db :chars char_file {:transaction? true})
        _ (log/trace "transaction" transaction)
        id (first transaction)
        _ (log/trace "id" id)
        ]
    (:generated_key id)))
(defn update-char
  "Replaces the char_file in the db with the new character file"
  ([id char-file]
   (jdb/update! db
                :chars
                {:char_file (pr-str char-file)
                 :char_name (:name char-file)}
                ["char_id = ?" id]))
  ([char-file]
   (update-char (:char_id char-file) char-file)))
(defn load-char-from-db
  "Takes an integer key, gets the data object from the database."
  [k]
  (-> (jdb/query db ["SELECT char_file FROM chars WHERE char_id = ?;" k])
      first
      :char_file
      edn/read-string
      (assoc :char_id k)
      )
  )
(defn get-char-ids
  "Returns a list of all character ids"
  []
  (map :char_id (jdb/query db ["SELECT char_id FROM chars"])))
(defn get-char-list
  "Returns a list of characters, both :char_id and :char_name"
  []
  (jdb/query db ["SELECT char_id, char_name FROM chars"]))
(defn get-filtered-char-list
  "Returns a list of characters starting with the specified letters"
  [s]
  (jdb/query db ["SELECT char_id, char_name FROM chars WHERE char_name LIKE CONCAT(?,'%');" s]))
(comment
  (get-filtered-char-list "test")
  (get-filtered-char-list "gh")
  (get-filtered-char-list "")
  )

;; Users
(defn save-user-to-db ;; TODO bcrypt
  "Takes a data object, saves it to the database.
  Returns the user with the new id, or nil on error"
  [{:keys [user_email user_pass user_name] :as user}]
  (if (or (not user_email)
          (not user_pass)
          (not user_name))
    (do
      (log/info "Missing field on user object! Keys:" (keys user))
      nil
      )
    (try
      (let [new-id
            (-> (jdb/insert! db :user user {:transaction? true})
                first
                :generated_key)]
        (if new-id
          (assoc user :user_id new-id)
          (do
            (log/info "Did not get returned id!")
            nil
            )
          )
        )
      ;(catch MySQLIntegrityConstraintViolationException e
      (catch Exception e
        (log/info "Could not insert new user, violated constraint:" e)
        nil
        )
      )
    )
  )
(defn load-user-by-email
  "Loads a user from the database by email
  Returns the user, or nil"
  [email]
  (first (jdb/query db ["SELECT * FROM user WHERE user_email LIKE ?;" email])))
(defn user-log-in ;; TODO bcrypt
  "Loads a user from the database, checks password, and if successful returns the user object without the password field.
  Returns nil on error."
  [email password]
  (if-let [u (load-user-by-email email)]
    (if (= password (:user_pass u))
      (dissoc u :user_pass)
      nil
      )
    nil
    )
  )
