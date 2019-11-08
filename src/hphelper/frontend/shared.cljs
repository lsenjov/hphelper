(ns hphelper.frontend.shared
  (:require [taoensso.timbre :as log]
            [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET POST] :as ajax]
            [goog.events :as events]
            )
  (:import [goog.events EventType])
  )

(defonce ^:private system-info
  (atom {:context (-> js/window
                      .-location
                      ;; This gets /context/index
                      .-href
                      ;; Remove the ending
                      (clojure.string/replace "/index" "")
                      )
         ;; Adds this to button objects, affects the padding.
         :buttonSize " btn-sm "
         ;; Display all information known to the client?
         ;; It's okay if clients turn this on themselves, they won't have any privileged information
         :debug false
         ;; Display tutorial text
         :tutorial true
         :skills [
                  {:skills_name "Management"
                   ;; Description of the skill
                   :skills_desc "Management: Navigating and manipulating Alpha Complex Bureaucracy; ordering people around; co-ordinating efforts; double-talk; double-think; increasing the number of illicit contacts."
                   ;; The initial used
                   :init "M"}
                  {:skills_name "Violence"
                   :skills_desc "Violence: Blowing things up; ordering others to blow things up; military tactics and protocols; making you hard to assassinate."
                   :init "V"}
                  {:skills_name "Subterfuge"
                   :skills_desc "Subterfuge: Stealth, surveillance, and skullduggery; deception; ordering assassinations and deniable missions; knowledge of security systems; sabotage; espionage and counter-espionage."
                   :init "Su"}
                  {:skills_name "Hardware"
                   :skills_desc "Hardware: Operation and construction of vehicles, bots, nuclear reactors and other machinery; knowledge of the cutting edge of theoretical sciences"
                   :init "H"}
                  {:skills_name "Software"
                   :skills_desc "Software: Manipulating computer records; processing large amounts of information; programming bots, vehicles, and other specialised software agents; manipulate the controlled economy; hack into communications records; change the programming of The Computer."
                   :init "So"}
                  {:skills_name "Wetware"
                   :skills_desc "Wetware: Biological and chemical sciences; cloning and genetic engineering; subliminal messaging and pharmatherapy; reducing the effects of genetic drift on yourself"
                   :init "W"}
                  {:skills_name "Other"
                   :skills_desc "Other: Assorted specialities that don't fit nicely into other categories"
                   :init "O"}
                  ]
         ;; Random items to label go here
         :others [
                  {:name "AF"
                   :desc "Armed Forces"}
                  {:name "CI"
                   :desc "Compliance Index"}
                  {:name "CP"
                   :desc "Central Processing Unit"}
                  {:name "HI"
                   :desc "Happiness Index"}
                  {:name "HP"
                   :desc "Housing, Preservation, and Development & Mind Control"}
                  {:name "IS"
                   :desc "Internal Security"}
                  {:name "LI"
                   :desc "Loyalty Index"}
                  {:name "PL"
                   :desc "Production, Logistics, and Commissary"}
                  {:name "PS"
                   :desc "Power Services"}
                  {:name "RD"
                   :desc "Research and Development"}
                  {:name "SI"
                   :desc "Security Index"}
                  {:name "TD"
                   :desc "Troubleshooter Dispatch"}
                  {:name "TS"
                   :desc "Technical Services"}
                  {:name "Clone Number"
                   :desc "All clones start at 1, and this increments for every termination. With the exception of High Programmers, it's rare for this to go above 12."}
                  {:name "Public Standing"
                   :desc "How popular you are. Ranges from 10 as adored by the masses, to -10 as secretly loathed"}
                  {:name "Mutation Power"
                   :desc "How powerful your mutation. The higher the power, the more likely it is to succeed and do what you need it to do."}
                  ]

         }
        )
  )

(defn wrap-context
  "Adds the current context to a url"
  [url]
  (str (:context @system-info) url)
  )

(defn wrap-unique-key
  "Given a collection, returns the collection with a unique key mapped to each column.
  Mainly because JS throwing warning after warning about it otherwise"
  [coll]
  (doall
    (map (fn [k e] ^{:key k} e)
       (range)
       coll)))

;; For easier class work
(defn add-button-size
  "Adds button size to the end of a string"
  [^String classString]
  (str classString " " (:buttonSize @system-info)))
(defn switcher-toolbar
  "Creates a toolbar of buttons for switching between pages.
  Switches the item at path in atom a.
  Items is a vector of [keyword label] pairs"
  [^Atom a ^Vec path ^Vec items]
  [:div {:class "btn-group"}
   (doall (map (fn [[k v]]
                 ^{:key (if k k :nil-key)}
                 [:span {:class (add-button-size (if (= (get-in @a path) k)
                                                   "btn btn-success"
                                                   "btn btn-secondary"
                                                   )
                                                 )
                         :onClick #(swap! a assoc-in path k)
                         }
                  v
                  ]
                 )
               items
               )
          )
   ]
  )
(defn text-input
  "Creates a text box for entering data. Switches the text at path in atom a."
  ([^Atom a ^Vec path]
   [:input {:type "text"
            :class "form-control"
            :value (get-in @a path)
            :on-change #(swap! a assoc-in path (-> % .-target .-value clojure.string/trim))
            }
    ])
  ([^Atom a ^Vec path ^String placeholder]
   [:input {:type "text"
            :class "form-control"
            :placeholder placeholder
            :value (get-in @a path)
            :on-change #(swap! a assoc-in path (-> % .-target .-value clojure.string/trim))
            }
    ])
  )
(defn debug-switcher-button
  "Switches the :debug tag in system-info"
  []
  [:span {:class (if (:debug @system-info) "btn btn-success btn-xs" "btn btn-secondary btn-xs")
          :onClick #(swap! system-info update-in [:debug] not)
          }
   "Debug"
   ]
  )
(defn tutorial-switcher-button
  "Switches the :tutorial tag in system-info"
  []
  [:span {:class (add-button-size (if (:tutorial @system-info) "btn btn-primary" "btn btn-secondary"))
          :onClick #(swap! system-info update-in [:tutorial] not)
          }
   "Tutorial"
   ]
  )
(defn tutorial-text
  "If :tutorial is true, will return the supplied text, else will return nil"
  [text]
  (if (:tutorial @system-info)
    [:span {:class "text-primary"}
     text
     ]
    nil))
(defn tutorial?
  "Returns if tutorial is set or not"
  []
  (:tutorial @system-info))
(defn get-debug-status
  "Gets the current debug status (true/false)"
  []
  (:debug @system-info))
(defn two-decimals
  "Prints a number 2 decimal places"
  [n]
  (-> n (* 100) int (/ 100)))

;; Getting public info from server and storing it locally
(defn get-societies
  "Gets the societies from system info"
  []
  (:societies @system-info)
  )
(defn get-mutations
  "Gets the mutations from system info"
  []
  (:mutations @system-info)
  )
(defn get-drawbacks
  "Gets the mutations from system info"
  []
  (:drawbacks @system-info)
  )
(defn get-specialties
  "Gets specialties from system info"
  []
  (:specialties @system-info)
  )

;; Actually get the above info if it's not already there
(defn init
  "Initialises empty parts of system-info. Returns an empty string"
  []
  ;; Load secret society info
  (if (not (:societies @system-info))
    (ajax/GET (wrap-context "/api/db/get-societies/")
              {:response-format (ajax/json-response-format {:keywords? true})
               :handler (fn [m]
                          (log/info "Got societies.")
                          (swap! system-info assoc :societies m)
                          )
               ;:params {}
               }
              )
    )
  ;; Load mutations info
  (if (not (:mutations @system-info))
    (ajax/GET (wrap-context "/api/db/get-mutations/")
              {:response-format (ajax/json-response-format {:keywords? true})
               :handler (fn [m]
                          (log/info "Got mutations.")
                          (swap! system-info assoc :mutations m)
                          )
               ;:params {}
               }
              )
    )
  ;; Load drawbacks info
  (if (not (:drawbacks @system-info))
    (ajax/GET (wrap-context "/api/db/get-drawbacks/")
              {:response-format (ajax/json-response-format {:keywords? true})
               :handler (fn [m]
                          (log/info "Got drawbacks.")
                          (swap! system-info assoc :drawbacks m)
                          )
               ;:params {}
               }
              )
    )
  ;; Get the skills from the database
  (if (not (:specialties @system-info))
    (ajax/GET (wrap-context "/api/db/get-skills/")
              {:response-format (ajax/json-response-format {:keywords? true})
               :handler (fn [m]
                          (log/info "Got specialties.")
                          (swap! system-info assoc :specialties m)
                          )
               }
              )
    )
  ""
  )

;; Split a skill line, wrap skills with descriptions
(defn- find-start
  "Checks a string, finds what object, if any, it starts with. Returns the entire map, or nil if none"
  [string category cat-key]
  (some #(if (clojure.string/starts-with? string (cat-key %)) % nil)
        (category @system-info))
  )
(defn- find-start-society-with-parent
  "Checks a string, finds if it starts with a society with or without a parent"
  [string has-parent?]
  (some #(if (clojure.string/starts-with? string (:ss_name %)) % nil)
        (filter :ss_parent (:societies @system-info))))
(defn- find-specialty
  "Checks a string, finds what specialty, if any, it starts with"
  [string]
  (find-start string :specialties :skills_name)
  )
(defn- find-all
  "Checks a string, searches among all they keys, wraps where appropriate"
  [string]
  (cond
    ;; Check skills
    (find-start string :skills :skills_name)
    (find-start string :skills :skills_name)
    ;; Check specialties
    (find-start string :specialties :skills_name)
    (find-start string :specialties :skills_name)
    ;; Check societies, those with parents first
    (find-start-society-with-parent string true)
    (find-start-society-with-parent string true)
    ;; Check societies, those without parents next
    (find-start-society-with-parent string false)
    (find-start-society-with-parent string false)
    ;(find-start string :societies :ss_name)
    ;(find-start string :societies :ss_name)
    ;; Check Mutations
    (find-start string :mutations :name)
    (find-start string :mutations :name)
    ;; Check other
    (find-start string :others :name)
    (find-start string :others :name)
    ;; Nothing, return string
    :nothing
    string
    )
  )

(defn- wrap-generic
  "Wraps a string of items with their descriptions. Returns a list of items"
  [line sep func]
  (->> (clojure.string/split line sep)
       (map (fn [string]
              (let [sp (func string)
                    ;; Find the description keyword, if a map was returned
                    k (if (map? sp)
                        (if-let [newk (some #{:skills_desc :ss_desc :desc} (keys sp))]
                          newk))
                    ;; If there's a key, then sp was a map, and we can get the description to put in the title
                    ;; If there's no key, there's no title
                    title (if k (k sp) "")]
                ^{:key sp}
                [:span {:title title}
                 string
                 ]
                )
              )
            )
       doall
       (interpose sep)
       )
  )
(defn wrap-any
  "Wraps a string (possibly separated) with their descriptions."
  ([line sep]
   (wrap-generic line sep find-all))
  ([line]
   (wrap-any line ", "))
  )
(defn wrap-skills
  "Wraps a string of skills with their descriptions. Returns a span of spans"
  [line sep]
  (wrap-generic line sep find-specialty)
  )
(defn wrap-skill-initial
  "Wraps a string's initial with the skill description"
  [line]
  (wrap-generic line ", " #(find-start % :skills :init))
  )

;; Return the entire system-info atom, only useful for debugging
(defn get-debug
  "Gets the entirety of system-info as a string"
  []
  [:div
   [:div (pr-str (keys @system-info))]
   [:div (pr-str @system-info)]
   ]
  )

;; User logging in and out
(defn is-logged-in?
  "Is the user logged in? If true, return the api key. If false, return nil"
  []
  (:apiKey @system-info)
  )
(defn log-in
  "Attempts to log the user in. If successful, adds :apiKey
  If false, adds the message to the :message queue"
  [email pass]
  (if (not (:apiKey @system-info))
    (ajax/GET (wrap-context "/api/user/login/")
              {:response-format (ajax/json-response-format {:keywords? true})
               :params {:user_email email :user_pass pass}
               :handler (fn [{:keys [apiKey error] :as userdata}]
                          (if (or error (not apiKey))
                            (swap! system-info update-in [:messages] conj {:message (str "Could not log in.")})
                            (do
                              (swap! system-info assoc :apiKey apiKey)
                              (swap! system-info assoc :userdata userdata)
                              )
                            )
                          )
               }
              )
    )
  )
(defn log-out
  "Clears user info"
  []
  (swap! system-info dissoc :apiKey :userdata)
  )

(defn user-bar-component
  "Component for users logging in and out"
  []
  (let [apiKey (:apiKey @system-info)
        {email :user_email :as userData} (:userdata @system-info)
        login-atom (atom {:email "Email" :pass "Pass"})
        ]
    (if apiKey
      ;; User is logged in already
      [:span {:class "nav navbar-nav"}
       (str "Logged in: " (get-in @system-info [:userdata :user_name]))
       [:span {:class (add-button-size "btn btn-default nav navbar-nav navbar-right")
               :onClick log-out
               }
        "Log Out"
        ]
       ]
      ;; User is not logged in
      ;; TODO email and pass as background text
      [:span {:class "nav navbar-nav"}
       [:span {:class "nav navbar-nav"}
        [:input {:type "text"
                 :class "form-control input-sm"
                 :value (get @login-atom :email)
                 :on-change #(swap! login-atom assoc :email (-> % .-target .-value))
                 }
         ]
        ]
       [:span {:class "nav navbar-nav"}
        [:input {:type "text"
                 :class "form-control input-sm"
                 :value (get @login-atom :pass)
                 :on-change #(swap! login-atom assoc :pass (-> % .-target .-value))
                 }
         ]
        ]
       [:span {:class "nav navbar-nav"}
        [:span {:class (add-button-size "btn btn-default")
                :onClick #(log-in (:email @login-atom) (:pass @login-atom))
                }
         "Log In"
         ]]])))

;; Draggable Components

;; Records position data for the entire session
(defonce pos-atom
  (atom {:_zindex 1}))
(defn get-client-rect [evt]
  (log/info "get-client-rect" evt)
  (let [r (.getBoundingClientRect (.-target evt))]
    {:left (.-left r), :top (.-top r)}))
(defn mouse-move-handler [title offset]
  (fn [evt]
    (log/info "mouse-move-handler")
    (let [x (- (.-clientX evt) (:x offset))
          y (- (.-clientY evt) (:y offset))
          ;; Pretty hacky, but gets the distance we've scrolled down/left
          ;; It's equivalent to $(js/window).scrollTop()
          scrollTop (.scrollTop ((js* "$") js/window))
          scrollLeft (.scrollLeft ((js* "$") js/window))
          ]
      (log/info "mouse-move-handler" x y scrollTop scrollLeft @pos-atom)
      (swap! pos-atom update-in [title] merge {:x (+ x scrollLeft) :y (+ y scrollTop)}))))
(defn- update-zindex-inner
  [m id]
  (let [highest (or (:_zindex m) 1)
        current (get-in m [id :zindex])]
    (if (< current highest) ;; If current is less than highest
      (-> m
          (update-in [:_zindex] inc) ;; Increment zindex tracker
          (assoc-in [id :zindex] (inc highest))) ;; And associate the new value with this zindex
      m)))
(defn- update-zindex
  "Ensures this now has the highest z index of any of the draggables"
  [id]
  (swap! pos-atom update-zindex-inner id))
(defn mouse-up-handler [id on-move]
  (fn me [evt]
    (log/info "mouse-up-handler")
    (events/unlisten js/window EventType.MOUSEMOVE
                     on-move)))
(defn mouse-down-handler
  [title e]
  (let [{:keys [left top]} (get-client-rect e)
        offset             {::x (- (.-clientX e) left)
                            ::y (- (.-clientY e) top)}
        on-move            ((partial mouse-move-handler title e) offset)]
    (log/info "mouse-down-handler" e)
    (events/listen js/window EventType.MOUSEMOVE
                   on-move)
    (events/listen js/window EventType.MOUSEUP
                   ((partial mouse-up-handler pos-atom) on-move))))
(defn min-or-maximize
  [title e]
  (log/info "min-or-maximize" title)
  (swap! pos-atom update-in [title :minimised?] not))
(defn comp-draggable
  [title body-comp {:keys [x y display-fn] :as ?start-coords} ?style-map]
  (log/info "comp-draggable")
  (swap! pos-atom update-in [title] #(or % ?start-coords {}))
  (let [default-styles {
                        :display "flex"
                        :overflow "auto"
                        }
        style (if ?style-map (merge default-styles ?style-map) default-styles)]
    (fn []
      (log/info "comp-draggable inner")
      [:div {:on-mouse-down (partial update-zindex title)}
       (if (get-debug-status)
         [:div (pr-str (get-in @pos-atom [title]))])
       [:div.card.border-secondary
        {:style (merge style
                       {:z-index (get-in @pos-atom [title :zindex])}
                       (if (get-in @pos-atom [title :undocked?])
                         {:position "absolute"
                          :left (or (get-in @pos-atom [title :x]) x 100)
                          :top (or (get-in @pos-atom [title :y]) y 100)
                          }
                         {}
                          ))}
        [:div.card-header.no-select
         ;:on-click #(rf/dispatch [::move-window :test {::x 200 ::y 200}])
         {:class (if (= (:_zindex @pos-atom) (get-in @pos-atom [title :zindex]))
                  "bg-primary text-white" "border-secondary")
          :on-mouse-down (partial mouse-down-handler title)}
         (if display-fn
           (display-fn)
           (str title " "))
         ;; Minimise/maximise button
         [:div.btn.btn-secondary.btn-sm
          {:on-click (partial min-or-maximize title)} ;; TODO
          (if (get-in @pos-atom [title :minimised?]) "\u21D2" "\u21D3")
          ]
         ;; Dock/undock button
         [:div.btn.btn-secondary.btn-sm
          {:on-click #(swap! pos-atom update-in [title :undocked?] not)}
          (if (get-in @pos-atom [title :undocked?]) "\u23CE" "\u23CF")
          ]
         ]
        (if (not (get-in @pos-atom [title :minimised?]))
          [:div.card-body
           {:style {:position "relative"
                    :width "100%"
                    }}
           [body-comp]
           ]
          )
        ]])))

(defn- draggable-button
  "Creates a single draggable button (for dragging linked windows)"
  [t]
   [:div.btn.btn-secondary.btn-outline-secondary.btn-sm
    {:on-click (partial update-zindex t)
     :on-mouse-down (partial mouse-down-handler t)}
    t]
   )
(defn- draggable-button-row
  "Creates a single row of buttons"
  [ts]
  [:div.btn-group-vertical (wrap-unique-key (map draggable-button ts))]
  )
(defn draggable-menu
  "Not draggable itself, but has a full list of components that are draggable, that players can re-drag"
  []
  (let [titles (->> @pos-atom
                    (filter (fn [[_ v]] (:undocked? v)))
                    keys
                    (remove keyword?)
                    wrap-unique-key
                    )]
  [:div.btn-group-vertical
   (->> titles
        ;(partition-all 8)
        sort
        (map draggable-button)
        wrap-unique-key
        )
   ]
  ))
