(ns hphelper.shared.encrypt
  (:require
    [clojure.tools.logging :as log]
    )
  )

(def key-prime 7919)

(defn int-hide
  "Hides an integer with the prime"
  [i]
  (if (string? i)
    (int-hide (Long/parseLong i))
    (+ (rand-int key-prime) (* key-prime i))))

(defn int-show
  "Unhides an integer with the prime"
  [i]
  (if (string? i)
    (int-show (Long/parseLong i))
    (long (/ i key-prime))))