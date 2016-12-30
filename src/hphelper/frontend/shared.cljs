(ns hphelper.frontend.shared
  (:require [taoensso.timbre :as log]
            [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET POST] :as ajax]
            ))

(defonce ^:private system-info
  (atom {:context (-> js/window
                      .-location
                      ;; This gets /context/index.html
                      .-pathname
                      ;; Remove the ending
                      (clojure.string/replace "/index.html" "")
                      )
         :buttonSize " btn-sm "
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
  ""
  )

;; Return the entire system-info atom, only useful for debugging
(defn get-debug
  "Gets the entirety of system-info as a string"
  []
  (pr-str @system-info)
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
