(ns hphelper.chargen.select
  (:require 
    [clojure.tools.logging :as log]
    [hphelper.chargen.charform :as cform]
    [hphelper.chargen.generator :as cgen]
    [hphelper.chargen.print :as cprint]
    [hphelper.shared.saveload :as sl]
    [hiccup.core :refer :all]
    [hphelper.shared.saveload :refer :all]
    ))

(defn print-select-page
  "Prints a page for selecting individual crisises"
  ([baseURL]
   (html [:html
          [:head [:title "HPHelper Scenario Generator"]]
          [:body
           [:h3 "Scenario Generator"]
           [:form {:action (str baseURL "/char/gen/")}
            "Generate New Scenario:"
            [:input {:type "submit" :value "New Character"}]
            ]
           [:br]
           [:form {:action "." :method "get"}
            [:select {:name "char_id"}
             (map (fn [i] [:option {:value (:char_id i)} (str (:char_name i) "(" (:char_id i) ")" )])
                  (reverse (sl/get-char-list)))
             ]
            [:input {:type "submit" :value "Load Character"}]
            ]
           ]
          ]
         )))
