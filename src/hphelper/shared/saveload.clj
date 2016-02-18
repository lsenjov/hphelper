(ns hphelper.shared.saveload
  (:require [clojure.java.jdbc :as jdb]
            [clojure.tools.logging :as log]
            [clojure.edn :as edn]
            ))

(def db {:subprotocol "mysql"
         :subname "//127.0.0.1:3306/hpsaveload"
         :user "fc"})

(defn save-scen-to-db
  "Takes a data object, saves it as an edn string in the database.
  Returns the generated key"
  [obj]
  (:generated_key (first (jdb/insert! db :scen {:scen_file (prn-str obj)}))))

(defn load-scen-from-db
  "Takes an integer key, gets the data object from the database."
  [k]
  (edn/read-string (:scen_file (first (jdb/query db ["SELECT scen_file FROM scen WHERE scen_id = ?;" k])))))

(defn get-scen-ids
  "Returns a list of all scenario ids"
  []
  (map :scen_id (jdb/query db ["SELECT scen_id FROM scen"])))
