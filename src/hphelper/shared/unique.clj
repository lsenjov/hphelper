(ns hphelper.shared.unique
  (:require [taoensso.timbre :as log]
            )
  (:gen-class)
)

(defn uuid
  "Generates a random uuid"
  []
  (str (java.util.UUID/randomUUID)))

(defn add-uuid-atom!
  "Adds an object to mapAtom, returns the uuid key"
  [ma o]
  (let [u (uuid)]
    (swap! ma assoc u o)
    u
    ))

(defn get-uuid-atom
  "Returns an object from a mapAtom"
  [ma uid]
  (@ma uid))

(defn swap-uuid!
  "Applys swap! to the correct item, returns the value object reffered by the uuid, or nil if invalid uuid"
  [ma uid f & args]
  (log/trace "swap-uuid!" ma uid f args)
  (if (and uid (@ma uid))
    (if args
      (let [newMap (apply f (@ma uid) args)]
        ((swap! ma assoc uid newMap) uid))
      ((swap! ma update-in [uid] f) uid))
    (do (log/trace "swap-uuid!: could not find uid:" uid)
        nil)))
