(ns hphelper.scengen.print
  (:require [hiccup.core :refer :all]
            [clojure.tools.logging :as log]
            [hphelper.shared.sql :as sql]
            ;; These are just here for debugging, should not be referenced anywhere else
            [hphelper.scengen.generator]
            [hphelper.scengen.scenform]
            )
  (:gen-class)
  )

(defn- html-print-crisis
  "Prints a single crisis in html format"
  [crisis]
  [:div
   [:b (crisis :c_desc)]
   [:br]
   (interpose (html [:br]) (crisis :extraDesc))
   [:br]
   ])

(defn- html-print-crisises
  "Prints all the crisises in html"
  [scenRec]
  (html [:div
         [:b "Welcome GM. Crisis numbers are: " (interpose ", " (map :c_id (scenRec :crisises))) [:br]]
         (map html-print-crisis (scenRec :crisises))
         ]))

(defn- html-print-directive-summary
  "Prints all the directives for the GM to view"
  [scenRec]
  (assert (scenRec :directives) "Directives do not exist?!")
  (println "Directives: " (scenRec :directives))
  (log/info "sg_ids: " (map :sg_id (scenRec :directives)))
  (html [:div
         [:b "Printing Directives:"]
         (interpose " -- "
                    (map
                      (fn [direc]
                        (assert direc "Direc is nil?!")
                        (assert (and (direc :sg_id) (direc :sgm_text)))
                        [:span [:b (sql/get-sg-by-id (direc :sg_id)) ": "] (direc :sgm_text)]))
                      (scenRec :directives))
         ]))

(defn- html-print-ssm-summary
  "Prints all the ssm for the GM to review"
  [scenRec]
  (html [:div
   [:small [:b "Secret Society Missions: "]
    (interpose " -- " (map
                     (fn [ssm] (str (sql/get-ss-by-id (ssm :ss_id)) ": " (ssm :ssm_text)))
                     (scenRec :societies)))
    ]]))

(defn html-print-scenario
  "Prints a scenario in html format"
  [scenRec]
  (html [:div
         (html-print-crisises scenRec)
         (html-print-directive-summary scenRec)
         (html-print-ssm-summary scenRec)
         ]
        ))
