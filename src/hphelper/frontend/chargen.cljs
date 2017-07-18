(ns hphelper.frontend.chargen
  "Page for creating and viewing characters"
  (:require [taoensso.timbre :as log]
            [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET POST] :as ajax]
            [hphelper.frontend.shared :refer [wrap-context add-button-size] :as shared]
            ))

(defonce system-atom
  (atom {:page nil})
  )

;; Where the character is stored. If it has a :char_id tag, is actually loaded from the database and we need to restrict options
(defonce character-atom
  (atom {})
  )

(def stats-all
  ["Violence" "Management" "Subterfuge" "Wetware" "Software" "Hardware"]
  )
(def stats-knowledge
  ["Hardware" "Software" "Wetware"]
  )
(def stats-action
  ["Management" "Subterfuge" "Violence"]
  )

(defn pad-left
  "Adds spaces to the left so it matches the length"
  [string len]
  (let [s (str string)
        s-len (count s)]
    (str (apply str (take (- len s-len)
                          (repeat \space))) ;; TODO force spaces instead of underscores
         string)
    )
  )

(defn stat-chooser
  ([^Vector stat-vec ^Atom c ^String label minimum maximum required]
   (fn []
     (let [v (get-in @c stat-vec)]
       [:span
        {:style {:align "right"}} ;; TODO
        [:span {:class "btn btn-default btn-xs" :onClick #(swap! c assoc-in  stat-vec nil)} "Reset"]
        [:span {:class "btn btn-default btn-xs" :onClick #(swap! c update-in stat-vec (fn [i] (min maximum (if i (inc i) minimum))))} "+"]
        [:span {:class "btn btn-default btn-xs" :onClick #(swap! c update-in stat-vec (fn [i] (max minimum (if i (dec i) maximum))))} "-"]
        (shared/wrap-any label ", ") " "
        [:span {:class (if (and required (not v))
                         "label label-info"
                         "label"
                         )}
                (pad-left (if v v "-") 2)
                ]
        ]
       )
     )
   )
  ([^Vector stat-vec ^Atom c ^String label minimum maximum]
   (stat-chooser stat-vec c label minimum maximum false)
   )
  )

(defn finalize-name
  "Finalizes the name section, returns an error message or nil"
  [^Atom c]
  (cond
    ;; Missing first name
    (= 0 (count (get-in @c [:nameParts :nameFirst])))
    "Missing First Name"
    ;; Missing last name
    (not (= 3 (count (get-in @c [:nameParts :zone]))))
    "Missing Three Character Zone (ABC, DEE, ZYX)"
    (not (get-in @c [:nameParts :cloneNum]))
    "Missing Clone Number"
    )
  )
(defn finalize-stats
  "Finalizes the stats section, returns an error message or nil"
  [^Atom c]
  (if-let [st
           (some identity
                 (map (fn [stat] (if (not (get-in @c [:priStats stat])) stat nil))
                      stats-all
                      )
                 )
           ]
    (str "Missing statistic: " st)
    )
  )
(defn finalize-mutation
  "Finalizes the mutation section, returns an error message or nil"
  [^Atom c]
  (cond
    ;; Missing mutation power
    (not (get-in @c [:mutation :power]))
    "Missing mutation power"
    ;; Missing
    (not (get-in @c [:mutation :total]))
    "Missing mutation count"
    )
  )
(defn finalize-societies
  "Finalizes the societies section, returns an error message or nil"
  [^Atom c]
  (let [picked (count (get-in @c [:programGroup]))
        required (get-in @c [:secStats "Program Group Size"])]
    (if (not (= picked required))
      (str "Have only picked " picked " societies. You need to pick " required " total.")
      )
    )
  )
(defn finalize-drawbacks
  "Finalizes the societies section, returns an error message or nil"
  [^Atom c]
  (if (and (not (get-in @c [:drawbacks]))
           (not (get-in @c [:drawbackCount])))
    "Missing drawback count"
    )
  )
(defn calculate-points-remaining
  "Calculates the points remaining to be spent. Assumes most nils are 0.
  If the character is a new character (missing :char_id), it will associate it in the map under :accessRemaining
  Otherwise, will just return :accessRemaining (if it exists)"
  [^Atom c]
  (let [stat-points (->> @c :priStats vals (filter identity) (reduce + 0))
        public-standing-points (if-let [ps (:publicStanding @c)] (* 2 ps) 0)
        mutation-points (-> @c :mutation :power (#(if % % 10)) (- 10))
        mutation-number (-> @c :mutation :total (#(if % % 1)) dec (* 20))
        ;; Drawback total is a negative number
        drawback-total (-> @c :drawbackCount (#(if % % 0)) (* -10))
        ]
    (if (and (:char_id @c) (:accessRemaining @c))
      ;; Don't touch points remaining
      (:accessRemaining @c)
      (let [pts (- 100 stat-points public-standing-points mutation-points mutation-number drawback-total)]
        (swap! c assoc :accessRemaining pts)
        pts
        )
      )
    )
  )
(defn display-points-remaining
  "Displays remaining points in a div"
  [^Atom c]
  (let [pts-rem (calculate-points-remaining c)
        perc (->> pts-rem (#(str % \%)))
        ]
    (cond
      (< pts-rem 0) ;; Negative points, full red bar
      [:div
       [:span {:class "label label-danger"} "ACCESS remaining: " pts-rem]
       [:div {:class "progress"}
        [:div {:class "progress-bar progress-bar-danger" :style {:width "100%"}}
         ]
        ]
       ]
      (< pts-rem 30) ;; Low points, warning
      [:div
       [:span {:class "label label-warning"} "ACCESS remaining: " pts-rem]
       [:div {:class "progress"}
        [:div {:class "progress-bar progress-bar-warning" :style {:width perc}}
         ]
        ]
       ]
      :else
      [:div
       [:span {:class "label label-basic"} "ACCESS remaining: " pts-rem]
       [:div {:class "progress"}
        [:div {:class "progress-bar progress-bar-basic" :style {:width perc}}
         ]
        ]
       ]
      )
    )
  )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Components
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn final-page-component
  [^Atom c]
  (fn []
    (let [pts-rem (calculate-points-remaining c)
          mes (or (finalize-name c)
                  (finalize-stats c)
                  (finalize-mutation c)
                  (finalize-societies c)
                  (finalize-drawbacks c)
                  )
          ]
      (cond
        ;; Error message
        mes
        [:div
         [:h4 "Item requires attention:"]
         [:div mes]
         ]
        ;; Negative points remain
        (< pts-rem 0)
        [:div
         [:h4 "Item requires attention:"]
         [:div "Negative points remain! Either free up points or take additional drawbacks (max 3)"]
         ]
        ;; No bad messages from elsewhere
        ;; Is this a new character?
        (not (get-in @c [:char_id]))
        [:div {:class (add-button-size "btn btn-success")
               :onClick #(ajax/POST (wrap-context "/api/char/new/")
                                   {:response-format (ajax/json-response-format {:keywords? true})
                                    :format :text
                                    :handler (fn [m]
                                               (log/info "Get char:" m)
                                               ;; Load completed char
                                               (reset! character-atom (cljs.reader/read-string (:char m)))
                                               )
                                    :params {:newchar (pr-str @character-atom)
                                             :debug "asdf"
                                             } ;; Move :lastUpdated somewhere
                                    }
                                   )
               }
         "Save Character"
         ]
        ;; It's an existing character, we need to update
        :existing
        [:div {:class (add-button-size "btn btn-success")
               :onClick #(ajax/POST (wrap-context "/api/char/update/")
                                   {:response-format (ajax/json-response-format {:keywords? true})
                                    :format :text
                                    :handler (fn [m]
                                               (log/info "Updated char")
                                               ;; Load completed char
                                               )
                                    :params {:newchar (pr-str @character-atom)
                                             :debug "asdf"
                                             } ;; Move :lastUpdated somewhere
                                    }
                                   )
               }
         "Update Character"
         ]
        )
      )
    )
  )

(defn- load-component
  "Component for loading characters from the database"
  [^Atom c]
  (let [charlist (atom '())]
    (fn []
      [:div {:class "col-lg-12"}
       [:div
        (shared/tutorial-text
          "If you already have a character, you can load it up and change your secret societies here. Newer characters appear first.
          Yes, it is possible to look at other player's sheets. Yes, getting caught will get you erased."
          )]
       [:div {:class "btn btn-warning"
              :onClick #(ajax/GET (wrap-context "/api/char/get-filtered/")
                                  {;:response-format (ajax/json-response-format {:keywords? true})
                                   :format :text
                                   :handler (fn [m]
                                              (log/info "Get char:" m)
                                              ;; Load completed char
                                              (reset! charlist (sort-by :char_id > (cljs.reader/read-string m)))
                                              )
                                   :params {:filter_string (js/prompt "Enter the first couple of letters of your character.")
                                            } ;; Move :lastUpdated somewhere
                                   }
                                  )}
        "Get characters by name"]
       (map (fn [{:keys [char_id char_name]}]
              ^{:key char_id}
              [:div {:class "btn btn-default"
                     :onClick #(ajax/GET (wrap-context "/api/char/get/")
                                         {:format :text
                                          :handler (fn [m]
                                                     (log/info "Got char:" m)
                                                     (reset! c (cljs.reader/read-string m)))
                                          :params {:char_id char_id}})}
               (str char_name " " char_id)])
            @charlist)])))


(defn- name-primary-component
  "Component for choosing name"
  [^Atom c]
  [:div {:class "col-lg-12"}
   [:div {:class "col-lg-4"}
    ;; Name
    [:label {:class "control-label"} "Name: "]
    [:input {:type "text"
             :class (if (= 0 (count (get-in @c [:nameParts :nameFirst]))) "form-control alert-info" "form-control")
             :value (get-in @c [:nameParts :nameFirst])
             :on-change #(swap! c assoc-in [:nameParts :nameFirst] (-> % .-target .-value))
             }
     ]
    ]
   [:div {:class "col-lg-4"}
    [:label {:class "control-label"} "Zone: "]
    [:input {:type "text"
             :class (if (not (= 3 (count (get-in @c [:nameParts :zone])))) "form-control alert-info" "form-control")
             :value (get-in @c [:nameParts :zone])
             :on-change #(swap! c assoc-in [:nameParts :zone] (->> % .-target .-value (take 3) (apply str) clojure.string/upper-case))
             }
     ]
    ]
   [:div {:class "col-lg-4"}
    [stat-chooser [:nameParts :cloneNum] c "Clone Number" 1 99 true]
    ]
   ]
  )
(defn- name-secondary-component
  "Compiles and displays name from parts"
  [^Atom c]
  (let [{:keys [nameFirst zone cloneNum]} (get @c :nameParts)
        fullName (str nameFirst "-U-" zone (if cloneNum (str "-" cloneNum) ""))
        ]
    (swap! c assoc :name fullName)
    [:div "Full Name: "
     [:h5 (:name @c)
      ]
     ]
    )
  )
(defn name-component
  "Page for choosing name"
  [^Atom c]
  (fn []
    [:div
     (shared/tutorial-text
       "The full name of a high programmer is along the lines of 'Name-U-ABC-4'. The first name is anything you choose. The U in the middle stand for ULTRAVIOLET (your clearance). The ABC can be any 3 letter combination representing your zone of decanting. The number on the end is your current clone number"
       )
     [name-primary-component c]
     [name-secondary-component c]
     ]
    )
  )

(defn- societies-single-component
  [^Atom c {:keys [ss_id ss_name ss_desc ss_type sskills] :as ss}]
  (let [status (atom {:selected false})]
    (fn []
      [:tr
       [:td>input {:type "checkbox"
                   :checked (if (get-in @c [:programGroup ss]) true false)
                   :onClick #(swap! c update-in [:programGroup]
                                    (fn [ss-set]
                                      (if ss-set ;; If the set already exists
                                        (if (some #{ss} ss-set) ;; If it's already picked
                                          (set (remove #{ss} ss-set)) ;; Remove it from the set
                                          (conj ss-set ss) ;; Add it to the set
                                          )
                                        #{ss} ;; Initialise the set
                                        )
                                      )
                                    )
                   }
        ]
       [:td ss_name ]
       [:td ss_type ]
       [:td (shared/wrap-any sskills ", ") ]
       [:td ss_desc ]
       ]
      )
    )
  )
(defn societies-component
  [^Atom c]
  [:div
   (shared/tutorial-text
     "Here you can pick the contacts that members of your program group have concact with. Class A societies are technically illegal but generall accepted. Class C societies are heavily illegal but very good at what they do. Class B is somewhere in the middle. Note that picking multiple sects of the same group will not give you multiple missions from the group, and should generally be avoided. "
     )
   (if-let [pgs (get-in @c [:secStats "Program Group Size"])]
     (str "You must pick " pgs " secret society contacts. ")
     )
   (str "You have picked " (-> @c :programGroup count) " secret society contacts.")
   [:table {:class "table table-striped"}
    [:thead
     [:tr
      [:th]
      [:th "Name"]
      [:th "Class"]
      [:th "Skills"]
      [:th "Description"]
      ]
     ]
    [:tbody
     (let [ss (shared/get-societies)]
       (map (fn [s] ^{:key s} [societies-single-component c s])
            (sort-by :ss_name ss))
       )
     ]
    ]
   ]
  )

(defn stats-secondary-component
  "Displays secondary stats"
  [^Atom c]
  (log/info "Calculating secondary stats")
  [:div
   (let [pg-size (-> @c (get-in [:priStats "Management"]) (#(if % (-> % dec (quot 5) (+ 2)) nil)))]
     (swap! c assoc-in [:secStats "Program Group Size"] pg-size)
     [:div
      "Program Group Size: "
      pg-size
      ]
     )
   (let [c-deg (-> @c (get-in [:priStats "Wetware"]) (#(if % (-> % dec (quot 5) - (+ 4)) nil)))]
     (swap! c assoc-in [:secStats "Clone Degredation"] c-deg)
     [:div
      "Clone Degredation: " c-deg
      ]
     )
   ]
  )

(defn mutation-component
  "Selector for mutations"
  [^Atom c]
  (let [muts (sort-by :name (shared/get-mutations))]
    [:div
     (display-points-remaining c)
     (shared/tutorial-text
       [:div
        [:p "Mutations are heavily treasonous (not terminable), but extremely powerful if used correctly. Your power determines the chance of success when using it. The mutation count is how many random powers your character has. Your first power is free and mandatory, but you may buy additional powers and 20 ACCESS apiece."]
        [:p "It is suggested new players only take a single mutation"]
        ]
       )
     ;; Power
     [:div
      [stat-chooser [:mutation :power] c "Mutation Power" 7 16 true]
      ]
     [:div
      [stat-chooser [:mutation :total] c "Mutation Count" 1 3 true]
      ]
     [:h3 "Possible Mutations:"]
     [:table {:class "table table-striped"}
      [:thead
       [:tr
        [:td "Mutation Name"]
        [:td "Mutation Description"]
        ]
       ]
      [:tbody
       (map (fn [{m-name :name m-desc :desc}] ^{:key m-name} [:tr [:td m-name] [:td m-desc]]) muts)
       ]
      ]
     ]
    )
  )

(defn stats-component
  [^Atom c]
  (fn []
    [:div {:class "bs-docs-section"}
     (display-points-remaining c)
     (shared/tutorial-text
       "Each statistic costs 1 ACCESS per point. The higher the skill, the more likely you will succeed at it."
       )
     [:div
      (map (fn [stat] ^{:key stat} [:div [stat-chooser [:priStats stat] c stat 1 20 true]]) stats-action)
      ]
     [:div
      (map (fn [stat] ^{:key stat} [:div [stat-chooser [:priStats stat] c stat 1 20 true]]) stats-knowledge)
      ]
     [:div
      [stats-secondary-component c]]]))

(defn drawbacks-component
  [^Atom c]
  (fn []
    [:div {:class "bs-docs-section"}
     (display-points-remaining c)
     (shared/tutorial-text
       [:p "Drawbacks can give you additional starting ACCESS, at the cost of hindering you during play.
           Some drawbacks are purely roleplay drawbacks, while some can debilitate you if you aren't skilled enough to play around them.
           Asking about changes to the drawback system will involve your character getting Impending Doom in addition to any other drawbacks."]
       )
     [:p "New players should take a maximum of a single drawback."]
     [stat-chooser [:drawbackCount] c "Drawback Count" 1 3 false]
     [:table {:class "table table-striped"}
      [:thead>tr>th "Possible Drawbacks"]
      [:tbody
       (map (comp (fn [desc] ^{:key desc} [:tr>td desc]) :text)
            (sort-by :text (shared/get-drawbacks))
            )]]]))

(defn public-standing-component
  [^Atom c]
  (fn []
    ;; Public Standing
    [:div {:class "bs-docs-section"}
     (display-points-remaining c)
     (shared/tutorial-text
       [:div
        [:p "If your public standing is nil, your character is unknown to the masses (those below BLUE clearance). If your public standing is positive, you are a star of the complex, and the masses adore you. The higher (up to 10) your public standing goes, the more the public adore you."]
        [:p "However, public standing can change considerably, changing up or down in a single session. It can also go into negatives. It goes up with public appearances, looking good, and generally appearing like an upstanding ULTRAVIOLET. It goes down whenever you're incompetent, publicly unhappy, or terminated for treason."]
        [:p "In mechanical terms, you gain your public standing in ACCESS at the beginning of each session. It is suggested new players leave this blank."]
        ]
       )
     [stat-chooser [:publicStanding] c "Public Standing" 1 10]
     ]
    )
  )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Pages
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn character-creation
  "Page for creating characters"
  []
  (let [c character-atom page-atom (atom {:page :name})]
    (fn []
      [:div
       ;; Page chooser
       [:div {:class "btn-group"}
        (shared/switcher-toolbar page-atom [:page]
                                 [[:name "Name"] [:stats "Statistics"] [:public "Public Standing"] [:mutation "Mutation"]
                                  [:societies "Secret Societies"] [:drawbacks "Drawbacks"] [:finalize "Finalize Character"] [:load "Load Character"]]
                                 )
        ]
       ;; TODO
       ;; Page viewer
       [:div
        (case (:page @page-atom)
          :name [name-component c]
          :stats [stats-component c]
          :public [public-standing-component c]
          :mutation [mutation-component c]
          :societies [societies-component c]
          :drawbacks [drawbacks-component c]
          :finalize [final-page-component c]
          :load [load-component c]
          )
        ]
       ;; Debug
       (if (shared/get-debug-status)
         [:div "Character atom:" @c]
         nil
         )
       ]
      )
    )
  )

(defn character-sheet-component
  "Displays all the non-modifiable objects from a character sheet"
  [^Atom c]
  [:div
   [:h5 "Citizen " (:name @c)]
   ;; Public Standing
   [:div {:class "col-lg-4"}
    [:div {:class "panel panel-default"}
     [:div {:class "panel-heading"}
      "Public Standing"
      ]
     [:div {:class "panel-body"}
      (if (:publicStanding @c)
        (:publicStanding @c)
        "None"
        )
      ]
     ]
    ]
   ;; Mutations
   [:div {:class "col-lg-4"}
    [:div {:class "panel panel-default"}
     [:div {:class "panel-heading"}
      "Mutations"
      ]
     [:div {:class "panel-body"}
      [:div "Mutation Power:" (get-in @c [:mutation :power])]
      [:div "Mutations:"]
      (->> (get-in @c [:mutation :description])
           (map (fn [{id :id m-name :name m-desc :desc}]
                  ^{:key id}
                  [:div m-name ": " m-desc]))
           doall
           )
      ]
     ]
    ]
   ;; Drawbacks
   [:div {:class "col-lg-4"}
    [:div {:class "panel panel-default"}
     [:div {:class "panel-heading"}
      "Drawbacks"
      ]
     [:div {:class "panel-body"}
      (->> (get-in @c [:drawbacks])
           (map :text)
           (map (fn [t] ^{:key t} [:div t]))
           doall
           )
      ]
     ]
    ]
   ]
  )

(defn character-modification
  "Modifies a character from the database"
  []
  (let [c character-atom page-atom (atom {:page :sheet})]
    (fn []
      [:div
       ;; Page chooser
       [:div {:class "btn-group"}
        (shared/switcher-toolbar page-atom [:page]
                                 [[:sheet "Display Sheet"]
                                  [:societies "Secret Societies"]
                                  [:finalize "Finalize Character"]
                                  [:load "Load Character"]]
                                 )
        ]
       ;; TODO
       ;; Page viewer
       [:div
        (case (:page @page-atom)
          ;; TODO
          :sheet [character-sheet-component c]
          :name [name-component c]
          :stats [stats-component c]
          :public [public-standing-component c]
          :mutation [mutation-component c]
          :societies [societies-component c]
          :drawbacks [drawbacks-component c]
          :finalize [final-page-component c]
          :load [load-component c]
          [:div "Make a selection"]
          )
        ]
       ;; Debug
       "Character atom:" @c
       ]
      )
    )
  )

(defn character-page
  "Top level page for creating and viewing characters"
  []
  (let [page (:page @system-atom)]
    [:div
     (if (:char_id @character-atom)
       ;; Creation
       [character-modification]
       [character-creation]
       )
     (if (shared/get-debug-status)
       [:div "System info:" [shared/get-debug]]
       nil
       )
     ]
    )
  )
