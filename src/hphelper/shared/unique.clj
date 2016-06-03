(ns hphelper.shared.unique

)

(defn uuid
  "Generates a random uuid"
  []
  (str (java.util.UUID/randomUUID)))

(defn add-uuid-atom
  "Adds an object to mapAtom, returns the uuid key"
  [ma o]
  (let [u (uuid)]
    (swap! ma assoc u o)
    u
    ))
