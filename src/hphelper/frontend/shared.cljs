(ns hphelper.frontend.shared
  (:require [taoensso.timbre :as log]
            [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET POST] :as ajax]
            ))

;; TODO return to defonce
(defonce ^:private system-info
  (atom {:context (-> js/window
                      .-location
                      ;; This gets /context/index.html
                      .-pathname
                      ;; Remove the ending
                      (clojure.string/replace "/index.html" "")
                      )
         :buttonSize " btn-sm "
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
                                                   "btn btn-default"
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
  [^Atom a ^Vec path]
  [:input {:type "text"
           :class "form-control"
           :value (get-in @a path)
           :on-change #(swap! a assoc-in path (-> % .-target .-value))
           }
   ]
  )

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
    ;; Check societies
    (find-start string :societies :ss_name)
    (find-start string :societies :ss_name)
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
         ]
        ]
       ]
      )
    )
  )
