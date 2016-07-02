(ns hphelper.shared.encrypt
  (:require
    [taoensso.timbre :as log]
    )
  )

(def keyNum (+ 2000 (rand-int 5000)))
(def offset (+ 10 (rand-int (- keyNum 20))))

(defn int-hide
  "Hides an integer with the prime"
  [i]
  (if (string? i)
    (int-hide (Long/parseLong i))
    (+ offset (* keyNum i))))

(defn int-show
  "Unhides an integer with the prime, returns nil if invalid"
  [i]
  (if (string? i)
    (int-show (Long/parseLong i))
    (if (= offset (mod i keyNum))
      (long (Math/floor (/ i keyNum)))
      nil)))
