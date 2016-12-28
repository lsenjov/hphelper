(ns hphelper.server.chargen.select
  (:require 
    [taoensso.timbre :as log]
    [hphelper.server.chargen.charform :as cform]
    [hphelper.server.chargen.generator :as cgen]
    [hphelper.server.chargen.print :as cprint]
    [hphelper.server.shared.saveload :as sl]
    [hiccup.core :refer :all]
    [hphelper.server.shared.saveload :refer :all]
    ))

(defn print-select-page
  "Prints a page for selecting individual crisises"
  ([baseURL]
   (html [:html
          [:head [:title "HPHelper Character Generator"]]
          [:body
           [:h3 "Character Generator"]
           [:form {:action (str baseURL "/char/gen/")}
            "Generate New Character:"
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
