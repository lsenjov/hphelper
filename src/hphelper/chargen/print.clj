(ns hphelper.chargen.print
  (:require [hphelper.shared.sql :as sql]
            [hiccup.core :refer :all]
            [taoensso.timbre :as log]
            [hphelper.shared.indicies :refer :all]
            [schema.core :as s]
            [hphelper.shared.schema :as ss]
    )
  )

;; Displaying the Character
(defn html-print-stats
  "Returns the primary stats of a character in a readable format"
  [charRec]
  (html [:div
            [:b "Primary Statistics"]
            [:table {:style "width:100%"}
             (for [row (partition-all 2 (charRec :priStats))]
               [:tr (for [cell row] [:td (key cell) ": " (val cell)])])]]
           ))

(defn html-print-secondary
  "Returns the secondary stats of a character in a readable format"
  [charRec]
  (html [:div
            [:b "Secondary Statistics"][:br]
            (interpose (html [:br]) (map (fn [[k v]] (str k ": " v)) (charRec :secStats)))]
           ))

(defn html-print-mutation
  "Returns the mutation of a character in a readable format"
  [charRec]
  (html [:div
            (str "Mutation Description: "
                 (-> charRec (:mutation) (:description))
                 (html [:br])
                 "Mutation Strength: "
                 (-> charRec (:mutation) (:power)))
            ]
           ))

(defn html-print-program-group
  "Returns the program group of a character in a readable format"
  [charRec]
  (html [:div
            [:b "Program Group"][:br]
            [:table {:style "width:100%"}
             [:tr [:td "Society"] [:td "Skills"] [:td "Cover Identity"]]
             (for [ss (or (charRec :programGroup) (charRec "Program Group"))]
               [:tr
                [:td (ss :ss_name)]
                [:td [:small (ss :sskills)]]])]
            ]
           ))

(defn html-print-public-standing
  "Returns the public standing of a character in a readable format"
  [charRec]
  (html
    [:div
     "Public Standing: "
     (if (charRec :publicStanding)
       (charRec :publicStanding)
       "None")
     ]
    ))

(defn html-print-drawbacks
  "Returns the drawbacks of a character in a readable format"
  [charRec]
  (if (charRec :drawbacks)
    (html [:div
              [:b "Drawbacks"][:br]
               (for [drawback (charRec :drawbacks)]
                 (str drawback (html [:br])))
               ])
    ""))

(defn html-print-service-groups
  "Prints a list of the service groups in a readable format"
  []
  (html
    [:div
     [:b "Service Group Bids"][:br]
      (for [group (sql/query "SELECT `sg_name` FROM `sg` ORDER BY `sg_name` ASC;")]
        (str (group :sg_name) "<br />"))
      ]
    ))

(defn html-print-remaining-access
  "Prints the remaining access in a readable format"
  [charRec]
  (html
    [:div
     [:b "Remaining Access: "]
     (charRec :accessRemaining)
     ]))

(defn html-print-name
  "Prints the name of the character in a readable format"
  [charRec]
  (html
    [:div
     [:large [:b
              (if (charRec :name)
                (charRec :name)
                "Unnamed-U-ARE")
              ]]
     ]
    ))

(defn html-print-sheet
  "Prints a character sheet in a readable format, forces printing on a separate page"
  [charRec]
  (log/trace "Printing Character sheet for:" (:name charRec))
  (html
    [:div {:style "page-break-before: always;"}
     (html-print-name charRec)
     (html-print-stats charRec)
     (html-print-secondary charRec)
     (html-print-mutation charRec)
     (html-print-public-standing charRec)
     (html-print-program-group charRec)
     (html-print-drawbacks charRec)
     (html-print-service-groups)
     (html-print-remaining-access charRec)
     ]))

(defn html-print-sheet-one-page
  "Prints a character sheet in a readable format, with html tags for single pages"
  [charRec]
  (html [:html (html-print-sheet charRec)]))
