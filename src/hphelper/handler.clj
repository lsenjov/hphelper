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

(defroutes app-routes
  ;; CHARACTERS
  (GET "/char/" {params :params baseURL :context}
       (if (:char_id params)
         (cprint/html-print-sheet-one-page (sl/load-char-from-db (:char_id params)))
         (csel/print-select-page baseURL)))
  (GET "/char/gen/" {baseURL :context}
       (cform/html-select-page))
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
  (GET "/char/print/" {params :params}
       (cprint/html-print-sheet-one-page (sl/load-char-from-db (:char_id params))))

  ;; SCENARIOS
  (GET "/scen/" {params :params baseURL :context} 
       (if (params :scen_id)
         (ssel/print-crisis-page (params :scen_id) baseURL)
         (ssel/print-select-page baseURL)))
  (GET "/scen/gen/" {baseURL :context} (sform/html-select-page baseURL))
  (POST "/scen/gen/"
        {params :params baseURL :context}
        (-> params
            (sform/from-select-to-scenmap)
            (sgen/create-scenario)
            (sl/save-scen-to-db)
            ((partial sform/html-scen-to-full-page baseURL))))
  (GET "/scen/full/" {params :params baseURL :context}
       (sform/html-scen-to-full-page baseURL (:scen_id params)))
  (POST "/scen/full/" {params :params baseURL :context}
        (-> params
            (sform/from-scenmap-to-full)
            (sl/save-fullscen-to-db)
            (ssel/print-crisis-page baseURL)))
  (GET "/scen/print/" {params :params}
       (sprint/html-print-optional (sl/load-fullscen-from-db (params :scen_id)) (keys params)))

  ;; OTHER
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
