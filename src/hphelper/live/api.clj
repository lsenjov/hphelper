(ns hphelper.live.api
  (:require [hphelper.shared.sql :as sql]
            [taoensso.timbre :as log]
            [schema.core :as s]
            [clojure.data.json :as json]
            [hphelper.live.control :refer [get-game swap-game!] :as lcon]
            )
  (:gen-class)
)

(defn get-indicies
  "Gets the indicies of a game"
  [^String gUid]
  (log/trace "get-indicies:" gUid)
  (if-let [gi (:indicies (get-game gUid))]
    (json/write-str {:status "ok" :indicies gi})
    (json/write-str {:status "error" :message "Game does not exist"})
    ))

(defn- is-admin-get-game
  "Returns the gamemap if the user is an admin, else nil"
  [^String gUid ^String uUid]
  (if-let [g (get-game gUid)]
    (if (= uUid (:adminPass g))
      g
      nil)
    nil))

(defn admin-debug
  "Gets the current gamemap, requires admin login"
  [^String gUid ^String uUid]
  (log/trace "admin-debug. gUid:" gUid "uUid:" uUid)
  (if-let [g (is-admin-get-game gUid uUid)]
      (json/write-str g)
      (json/write-str {:status "error" :message "Invalid game or user id"})
    ))

(defn admin-modify-index
  "Modifies an index by the required amount"
  [^String gUid ^String uUid ^String ind ^String amount]
  (log/trace "admin-modify-index." gUid uUid ind amount)
  (if-let [g (is-admin-get-game gUid uUid)]
    (if (lcon/modify-index gUid
                           (keyword ind)
                           (try (Integer/parseInt amount)
                                (catch Exception e
                                  (log/debug "Could not parse:" amount "Defaulting to 0")
                                  0)))
      (json/write-str {:status "ok"})
      (json/write-str {:status "error" :message "modify-index failed"}))
    (json/write-str {:status "error" :message "Invalid game or user id"})
    ))