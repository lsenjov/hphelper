(ns hphelper.scengen.scenform
  (:require [hphelper.shared.sql :as sql]
            [hiccup.core :refer :all]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [hphelper.chargen.generator :as cgen]
            [clojure.tools.logging :as log]
            [hphelper.shared.saveload :as sl]
            [hphelper.scengen.print :as sprint]
            )
  (:gen-class)
  )

(def numPlayers 
  "The number of players to display and print" 
  9)

;; This is the form presented to the player
(defn- html-old-player-sheet
  "Generates an input form for a single player"
  [playerId]
  (html [:div
         "Name:" [:input {:type "text" :name (str "name_" playerId)}] [:br]
         "Print Character Sheet?" [:input {:type "checkbox" :name (str "charsheet_" playerId)}] [:br]
         (map (fn [society] (str (society :ss_name)
                                 (html [:input {:type "checkbox" :name (str "ss_" playerId "_" (society :ss_id))}])
                                 (html [:br])))
              (sql/query "SELECT * FROM ss;"))
         "Player messages:" [:br]
         [:textarea {:rows "4" :name (str "messages_" playerId)}]
         ]))

(defn- html-player-sheet
  "Generates a selection form for a single player"
  [playerId]
  (html [:div
         "Character:" [:select {:name (str "char_" playerId)}
                       (conj
                         (reverse (map (fn [x] [:option {:value (:char_id x)}
                                                (str (:char_name x) \( (:char_id x) \) )])
                                       (sl/get-char-list)))
                         [:option {:value ""} "No Char"])
                       ]
         "Additional Messages for player:"
         [:textarea {:rows "4" :name (str "messages_" playerId)}]
         ]
        ))

(defn html-select-page
  "Generates a html page to select options for the creation of a scenario"
  [baseURL]
  (html
    [:html
     [:head
      [:title "Welcome High Programmer"]]
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
      [:form {:action (str baseURL "/scen/gen/") :method "post"}
       (anti-forgery-field)
       ;[:div "Random Seed:" [:input {:type "text" :name "seed"}] "(Numeric only, leave blank for random.)"]
       [:div "Sector Name:" [:input {:type "text" :name "s_name"}] "(Leave blank for random)"]
       [:div "Crisis Numbers:" (for [cField (range 3)]
                                 [:input {:type "text"
                                          :name (str "crisis_" cField)
                                          :pattern "\\d*"}])]
       ; Players table removed for second stage
       ; [:table [:tr (for [playerId (range numPlayers)] (html [:td (html-player-sheet playerId)])) ] ]
       "Extra cbay items: (Remember to add the price as \"5 ACCESS\" on the end)" [:br]
       [:textarea {:rows "4" :cols "80" :name "cbay"}]

       [:div [:input {:type "submit" :value "Create Sector"}]]
       ]

      ]
     ]
    )
  )

(defn html-scen-to-full-page
  "Generates a html page to select players for a created scenario"
  [baseURL scenId]
  (html
    [:html
     [:head
      [:title "Welcome High Programmer"]]
     [:body
      [:h3 "Select HPs for saved scenario: " scenId]
      [:form {:action (str baseURL "/scen/full/") :method "post"}
       (anti-forgery-field)
       [:input {:type "hidden" :name "scen_id" :value scenId}]
       [:table
        [:tr
         (for [playerId (range numPlayers)]
           (html [:td (html-player-sheet playerId)]))
         ]
        ]
       [:div [:input {:type "submit" :value "Create HPs"}]]
       ]
      (sprint/html-print-optional (sl/load-scen-from-db scenId) [:gmheader :gmcrisises :gmcbay :gmdirectives])
      ]
     ]
    ))
      

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
                    (range numPlayers))
          )
   params))

(defn- assoc-additional-messages
  "Checks for additional messages, and if exists and long enough splits by \n"
  [playerId
   {admes (keyword (str "messages_" playerId)) :as params}
   scen]
  (log/debug "playerId is" playerId ", keys are:" (apply str (keys scen)))
  (if (and admes
           (> (count admes) 0))
    (do 
      (assoc-in scen [:hps playerId :msgs]
                 (clojure.string/split admes #"\n"))
      )
    scen))

(defn- gen-character
  "Creates the numPlayers characters from whatever details given"
  [playerId params]
  (update-in params [:hps playerId] cgen/create-character))

(defn- assoc-saved-player
  "Taking a single player id, loads the character from the database and adds it to the scenario"
  [playerId params scen]
  (log/debug "playerId is" playerId ", keys are:" (apply str (keys scen)))
  (let [pInt (sql/parse-int ((keyword (str "char_" playerId)) params))]
    (if pInt
      (let [player (sl/load-char-from-db pInt)]
        (if player
          (assoc-additional-messages
            playerId
            params
            (assoc-in scen [:hps playerId] player))
          scen))
      scen)))

(defn- assoc-player-old
  "Given a player ID, checks the map for all items pertinent to the player and re-orders the map"
  [playerId params]
  (->> params
      (assoc-player-name playerId)
      (assoc-player-societies playerId)
      (assoc-additional-messages playerId)
      (gen-character playerId)
      ))

(defn- assoc-all-players
  "Associates all numPlayers possible players"
  ([params scen]
  (let [pIds (range numPlayers)]
    (-> scen
        ((apply comp
                (map partial
                     (repeat assoc-saved-player)
                     pIds
                     (repeat params)))) ;; Composes numPlayers assoc-player functions together, one for each possible player
        ))
  ))

(defn- assoc-all-crisises
  "Associates the crisises in the correct place"
  [params]
  (assoc-in params [:crisises] (into [] (map #(try (Integer/parseInt %) (catch NumberFormatException e nil))
                                             (remove nil?
                                                     (map (comp params keyword)
                                                          (map (partial str "crisis_")
                                                               (range 3))))))))

(defn- assoc-zone
  "Associates the crisis' zone in the correct field"
  [params]
  (assoc-in params [:zone] (get params :s_name)))

(defn- assoc-cbay
  "If cbay exists, converts to a vector of strings, if not, removes it"
  [{cbay :cbay :as params}]
  (if (and cbay
           (> (count cbay) 0))
    (assoc-in params [:cbay] (clojure.string/split cbay #"\n"))
    (dissoc params :cbay)))

(defn from-select-to-scenmap
  "Converts the form input to a scenario form for use by the generator"
  [params]
  (-> params
      (assoc-zone)
      (assoc-cbay)
      (assoc-all-crisises)
      ))

(defn from-scenmap-to-full
  "Converts the form input of a scenmap to a full scenario"
  [params]
  (log/info "Params in from-scenmap-to-full are:" params)
  (let [scen (sl/load-scen-from-db (sql/parse-int (:scen_id params)))]
    (assert scen (str "Scenario not found! ScenId is:" scen))
    (assoc-all-players params scen))
  )
