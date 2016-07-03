(ns hphelper.live.api
  (:require [hphelper.shared.sql :as sql]
            [taoensso.timbre :as log]
            [schema.core :as s]
            [clojure.data.json :as json]
            [hphelper.live.control :refer [get-game swap-game!]]
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

(defn admin-debug
  "Gets the current gamemap, requires admin login"
  [^String gUid ^String uUid]
  (log/trace "admin-debug. gUid:" gUid "uUid:" uUid)
  (if-let [g (get-game gUid)]
    (if (= uUid (:adminPass g))
      (json/write-str g)
      (json/write-str {:status "error" :message "Invalid User"})
      )
    (json/write-str {:status "error" :message "Game does not exist"})
    ))
