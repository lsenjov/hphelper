(ns hphelper.server.shared.unique
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
  "Applys swap! to the correct item, returns the value object reffered by the uuid,
  or nil if invalid uuid. If an exception is thrown while applying the function,
  will carry the exception up and not change the atom"
  [ma uid f & args]
  (log/trace "swap-uuid!" ma uid f args)
  (if (and uid (@ma uid))
    (if args
      ; swap! ma update-in [uid] (partial apply f) &args
      ; update-in ma [uid] (partial apply f) &args
      ; (partial apply f) mp &args
      ; apply f mp &args
      ; f mp args
      ((swap! ma update-in [uid] (partial apply f) args) uid)
      ((swap! ma update-in [uid] f) uid))
    (do (log/trace "swap-uuid!: could not find uid:" uid)
        nil)))
