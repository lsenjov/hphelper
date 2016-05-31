(ns hphelper.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]

            ;; Character handling
            [hphelper.chargen.generator :as cgen]
            [hphelper.chargen.charform :as cform]
            [hphelper.chargen.print :as cprint]
            [hphelper.chargen.select :as csel]

            ;; Scenario handling
            [hphelper.scengen.scenform :as sform]
            [hphelper.scengen.generator :as sgen]
            [hphelper.scengen.print :as sprint]
            [hphelper.scengen.select :as ssel]
            [hphelper.shared.saveload :as sl]
            [hiccup.core :refer :all]
            )
  (:gen-class))

(defroutes
  app-routes
  ;; CHARACTERS
  ;; Show page to select from list or create new character
  (GET "/char/" {params :params baseURL :context}
    (if (:char_id params)
      (cprint/html-print-sheet-one-page (sl/load-char-from-db (:char_id params)))
      (csel/print-select-page baseURL)))
  ;; Select options to create a new character
  (GET "/char/gen/" {baseURL :context}
    (cform/html-select-page))
  ;; Create a new character from the options above and save it
  (POST "/char/gen/"
        {params :params}
    (html [:div
           (-> params
               (cform/convert-to-char)
               (cgen/create-character)
               (sl/save-char-to-db)
               (sl/load-char-from-db)
               (cprint/html-print-sheet-one-page))
           ]))
  ;; Display a character
  (GET "/char/print/" {params :params}
    (cprint/html-print-sheet-one-page (sl/load-char-from-db (:char_id params))))

  ;; SCENARIOS
  ;; Show page to create a new scenario, or select a partially completed or fully completed scenario
  ;; If contains a :scen_id, loads completed scenario and allows options for printing parts or all
  (GET "/scen/" {params :params baseURL :context}
    (if (params :scen_id)
      (ssel/print-crisis-page (params :scen_id) baseURL)
      (ssel/print-select-page baseURL)))
  ;; Show page to create a new scenario
  (GET "/scen/gen/" {baseURL :context} (sform/html-select-page baseURL))
  ;; Create a brand new scenario without programmers picked
  (POST "/scen/gen/"
        {params :params baseURL :context}
    (-> params
        (sform/from-select-to-scenmap)
        (sgen/create-scenario)
        (sl/save-scen-to-db)
        ((partial sform/html-scen-to-full-page baseURL))))
  ;; Get scenario without programmers in order to pick programmers
  (GET "/scen/full/" {params :params baseURL :context}
    (sform/html-scen-to-full-page baseURL (:scen_id params)))
  ;; Add programmers to scenario and create a new record
  (POST "/scen/full/" {params :params baseURL :context}
    (-> params
        (sform/from-scenmap-to-full)
        (sl/save-fullscen-to-db)
        (ssel/print-crisis-page baseURL)))
  ;; Prints an entire scenario with options, including sheets for each character if desired
  (GET "/scen/print/" {params :params}
    (sprint/html-print-optional (sl/load-fullscen-from-db (params :scen_id)) (keys params)))
  ;; Prints a single session sheet for a character for a specific character
  (GET "/scen/print/char/" {params :params}
    (println params)
    (let [scen (sl/load-fullscen-from-db (Integer/parseInt (:scen_id params)))]
      (println (:hps scen))
      (sprint/html-print-player-sheet scen ((:hps scen) (Integer/parseInt (:p_id params))))))

  ;; OTHER
  ;; Simple directs to the above
  (GET "/" {baseURL :context}
    (html [:html
           [:body
            [:a {:href (str baseURL "/scen/")} "Scenario Generator"][:br]
            [:a {:href (str baseURL "/char/")} "Character Generator"][:br]
            [:br]
            [:a {:href "https://github.com/lsenjov/hphelper"} "Source Code"][:br]
            ]]))
  (route/not-found
    "Not Found.")
  )

(def app
  (-> app-routes
      (wrap-defaults site-defaults)
      ring.middleware.session/wrap-session
      )
  )
