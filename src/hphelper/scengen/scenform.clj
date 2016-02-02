(ns hphelper.scengen.scenform
  (:require [hphelper.shared.sql :as sql]
            [hiccup.core :refer :all]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [hphelper.chargen.generator :as cgen]
            [clojure.tools.logging :as log]
            )
  (:gen-class)
  )


;; This is the form presented to the player
(defn- html-player-sheet
  "Generates an input form for a single player"
  [playerId]
  (html [:div
         "Name:" [:input {:type "text" :name (str "name_" playerId)}] [:br]
         "Print Character Sheet?" [:input {:type "checkbox" :name (str "charsheet_" playerId)}]
         (map (fn [society] (str (society :ss_name)
                                 (html [:input {:type "checkbox" :name (str "ss_" playerId "_" (society :ss_id))}])
                                 (html [:br])))
              (sql/query "SELECT * FROM ss;"))
         [:textarea {:rows "4" :name (str "messages_" playerId)}]
         ]))

(defn html-select-page
  "Generates a html page to select options for the creation of a scenario"
  []
  (html
    [:html
     [:head
      [:title "Welcome High Programmer"]
      [:body
       [:h1 "Please make your selection below"]
       [:div "Some notes:" [:br]
        [:ul
         [:li "You can use a double hash to sub objects:"]
         [:li "##LOC-1## for example, pulls a random location. ##LOC-1## is used for many of the generic SS missions, and is often a good pick."]
         [:li "##CIT-G-1## will give you a random GREEN citizen's name. These names work from R all the way up to U"]
         [:li "##RES-1## will give a random resource. Anything from Nuclear Waste to Hot Fun. Rare in the missions, but may be useful"]
         [:li "##ZON## will return the zone's three letters."]
         ]
        ]
       ;; Description done, now for the actual form
       [:form {:action "/scen/" :method "post"}
        (anti-forgery-field)
        [:div "Random Seed:" [:input {:type "text" :name "seed"}] "(Numeric only, leave blank for random.)"]
        [:div "Sector Name:" [:input {:type "text" :name "s_seed"}] "(Leave blank for random)"]
        [:div "Crisis Numbers:" (for [cField (range 3)]
                                  [:input {:type "text"
                                           :name (str "crisis_" cField)
                                           :pattern "\\d*"}])]
        [:table
         [:tr
          (for [playerId (range 6)]
            (html [:td (html-player-sheet playerId)]))
          ]
         ]


        [:div [:input {:type "submit" :value "Create Sector"}]]
        ]

       ]
      ]
     ]
    )
  )


;; This is the conversion from the form params to a usable scenario generator map
(defn- assoc-player-name
  "Checks the map for the player's name, and if exists associates it in the player's index"
  [playerId params]
  (let [pKey (keyword (str "name_" playerId))
        pName (params pKey)]
    (if (> (count pName) 0) ;; If the name exists, put it into the player
      (assoc-in params [:hps playerId :name] pName) ;; Creates a new hashmap if none exist
      params)))

(defn- assoc-player-society
  "Checks for a single secret society, and if exists associate as needed"
  [playerId ssId params]
  (let [ssKey (keyword (str "ss_" playerId "_" ssId))]
    (if (contains? params ssKey)
      (update-in params [:hps playerId "Program Group"] (partial clojure.set/union #{}) #{(sql/get-society ssId)})
      params)))

(defn- assoc-player-societies
  "Checks for secret societies for a player, if they exist associate them as required"
  [playerId params]
  (let [societies (map :ss_id (sql/query "SELECT * FROM ss;"))]
    ((apply comp
            (map partial
                 (repeat assoc-player-society)
                 (repeat playerId)
                 societies))
     params)))

(defn- assoc-print-sheet
  "Checks to see which players need character sheets printed, and associates
  it with a set :printSheet keyword in the character"
  [params]
  ((apply comp (map (fn [pId]
                      (fn [params]
                        (if (contains? params (keyword (str "charsheet_" pId)))
                          (assoc-in params [:hps pId :printSheet] true)
                          params)))
                    (range 6))
          )
   params))

(defn- gen-character
  "Creates the 6 characters from whatever details given"
  [playerId params]
  (update-in params [:hps playerId] cgen/create-character))

(defn- assoc-player
  "Given a player ID, checks the map for all items pertinent to the player and re-orders the map"
  [playerId params]
  (->> params
      (assoc-player-name playerId)
      (assoc-player-societies playerId)
      (gen-character playerId)
      ))

(defn- assoc-all-players
  "Associates all 6 possible players"
  [params]
  (let [pIds (range 6)]
    (-> params
        ((apply comp
                (map partial
                     (repeat assoc-player)
                     pIds))) ;; Composes 6 assoc-player functions together, one for each possible player
        ))
  )

(defn- assoc-all-crisises
  "Associates the crisises in the correct place"
  [params]
  (assoc-in params [:crisises] (into [] (map #(try (Integer/parseInt %) (catch NumberFormatException e nil))
                                             (remove nil?
                                                     (map (comp params keyword)
                                                          (map (partial str "crisis_")
                                                               (range 3))))))))

(defn from-select-to-scenmap
  "Converts the form input to a scenario form for use by the generator"
  [params]
  (-> params
      (assoc-all-players)
      (assoc-all-crisises)
      (assoc-print-sheet)
      ))
