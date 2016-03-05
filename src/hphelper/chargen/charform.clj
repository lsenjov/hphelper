(ns hphelper.chargen.charform
  (:require [hphelper.shared.sql :as sql]
            [hiccup.core :refer :all]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [hphelper.chargen.generator :as cgen]
            [clojure.tools.logging :as log]
            ))

;; Display the sheet
;; Don't forget about the anti-forgery thing

(defn html-select-page
  "Generates a html page to select options for the creation of a character"
  []
  (html
    [:html
     [:head
      [:title "Welcome potential High Programmer"]
     [:body
      [:h1 "Select your statistics below"]
      [:div "Leave fields blank for random fills"]
      ;; Create the form
      [:form {:action "." :method "post"}
       (anti-forgery-field)
       ;; Name
       [:div "HP Name:" [:input {:type "text" :name "name"}]]
       ;; Stats
       (map (fn [statName] [:div statName [:select {:name (str "stat_" statName)}
                                           (concat '([:option "random"])
                                                   (map (fn [i] [:option {:value i} i])
                                                        (range 1 21)))
                                           ]])
            ["Management" "Subterfuge" "Violence" "Hardware" "Software" "Wetware"])
       ;; Drawbacks

       ]
      ]
     ]
     ]
    ))
