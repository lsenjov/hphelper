(ns hphelper.chargen.charform
  (:require [hphelper.shared.sql :as sql]
            [hiccup.core :refer :all]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [hphelper.chargen.generator :as cgen]
            [clojure.tools.logging :as log]
            ))

;; Display the sheet
;; Don't forget about the anti-forgery thing
(def statNames
  "The 6 stat names"
  ["Management" "Subterfuge" "Violence" "Hardware" "Software" "Wetware"])

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
       [:br]

       ;; Public Standing
       [:div "Select public standing, if any" 
        [:select {:name "PublicStanding"}
         (concat '([:option "No Public Standing"])
                 (map (fn [i] [:option {:value i} i])
                      (range 11)))
         ]
        ]

       ;; Drawbacks
       [:div "Select number of drawbacks:"
        [:select {:name "DrawbackCount"}
         (map (fn [i] [:option {:value i} i])
              (range 4))
         ]
        ]

       ;; Societies
       [:br]
       [:div "Select societies. If you select too few, they will be added randomly. If too many, they will be dropped randomly."]
       (map (fn [society] (str (society :ss_name)
                               (html [:input {:type "checkbox" :name (str "ss_" (society :ss_id))}])
                               (html [:br])))
            (sql/query "SELECT * FROM ss;"))

       [:div [:input {:type "submit" :value "Create Character"}]]
       ]
      ]
     ]
     ]
    ))

(defn- parse-int
  "Parses a string to an integer. On fail, return nil."
  [i]
  (if (integer? i)
    i
    (try (Integer/parseInt i)
         (catch Exception e nil))))

;; Converting from sheet to character sheet
(defn- assoc-name
  "If a name field exists, move it to the target"
  [params target]
  (if (> (count (:name params)) 0)
    (assoc target :name (:name params))
    target))

(defn- assoc-stat
  "Associates a single stat"
  [params stat target]
  (let [statVal (parse-int ((keyword (str "stat_" stat)) params))]
    (if statVal
      (assoc-in target [:priStats stat] statVal)
      target)))

(defn- assoc-stats
  "Associates the 6 stats"
  [params target]
  ((apply comp (map partial (repeat assoc-stat) (repeat params) statNames)) target))

(defn- generate-drawbacks
  "If drawbacks is greater than 0, generate that number of drawbacks for the character"
  [params target]
  (if (> (parse-int (:DrawbackCount params)) 0)
    ((apply comp (repeat (parse-int (:DrawbackCount params)) cgen/create-drawback)) target)
    target)
  )

(defn- assoc-public-standing
  "Associates public standing if it exists"
  [params target]
  (let [ps (parse-int (params :PublicStanding))]
    (if ps
      (assoc target :publicStanding ps)
      target)))

(defn convert-to-char
  "Coverts a set of paramaters to a data object to generate"
  [params]
  (->> {}
       (assoc-name params)
       (assoc-stats params)
       (generate-drawbacks params)
       (assoc-public-standing params)
  ;; Assoc Public Standing
  ;; Assoc Societies
  ))
