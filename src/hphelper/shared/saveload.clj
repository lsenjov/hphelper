(ns hphelper.shared.saveload
  (:require [clojure.java.jdbc :as jdb]
            [clojure.edn :as edn]
            [taoensso.timbre :as log]
            ))

(def db {:subprotocol "mysql"
         :subname "//127.0.0.1:3306/hpsaveload"
         :user "fc"})

;; Scenarios
(defn save-scen-to-db
  "Takes a data object, saves it as an edn string in the database.
  Returns the generated key"
  [obj]
  (log/trace "save-scen-to-db:" obj)
  (:generated_key (first (jdb/insert! db :scen {:scen_file (prn-str obj)}))))

(defn load-scen-from-db
  "Takes an integer key, gets the data object from the database."
  [k]
  (log/trace "load-scen-from-db. Scenario number:" k)
  (let [in (:scen_file (first (jdb/query db ["SELECT scen_file FROM scen WHERE scen_id = ?;" k])))]
    (log/trace "load-scen-from-db:" in)
    (edn/read-string in)))

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
  [k]
  (edn/read-string (:fs_file (first (jdb/query db ["SELECT fs_file FROM fullscen WHERE fs_id = ?;" k])))))

(defn get-fullscen-ids
  "Returns a list of all scenario ids"
  []
  (map :fs_id (jdb/query db ["SELECT fs_id FROM fullscen"])))

;; Characters
(defn save-char-to-db
  "Takes a data object, saves it as an edn string in the database.
  Returns the generated key"
  [obj]
  (:generated_key (first (jdb/insert! db :chars {:char_file (prn-str obj) :char_name (:name obj)} :transaction? true))))

(defn load-char-from-db
  "Takes an integer key, gets the data object from the database."
  [k]
  (edn/read-string (:char_file (first (jdb/query db ["SELECT char_file FROM chars WHERE char_id = ?;" k])))))

(defn get-char-ids
  "Returns a list of all character ids"
  []
  (map :char_id (jdb/query db ["SELECT char_id FROM chars"])))

(defn get-char-list
  "Returns a list of characters, both :char_id and :char_name"
  []
  (jdb/query db ["SELECT char_id, char_name FROM chars"]))
