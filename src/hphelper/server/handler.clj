(ns hphelper.server.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [taoensso.timbre :as log]
            [clojure.data.json :as json]

            ;; Character handling
            [hphelper.server.chargen.generator :as cgen]
            [hphelper.server.chargen.charform :as cform]
            [hphelper.server.chargen.print :as cprint]
            [hphelper.server.chargen.select :as csel]

            ;; Scenario handling
            [hphelper.server.scengen.scenform :as sform]
            [hphelper.server.scengen.generator :as sgen]
            [hphelper.server.scengen.print :as sprint]
            [hphelper.server.scengen.select :as ssel]
            [hphelper.server.shared.saveload :as sl]
            [hiccup.core :refer :all]

            ;; Shared items
            [hphelper.server.shared.sql :as sql]

            ;; Sector Generator
            [hphelper.server.sectorgen.generator :as secgen]

            ;; Live paranoia
            [hphelper.server.live.view :as lview]
            [hphelper.server.live.api :as lapi]

            ;; For hiding items from players
            [hphelper.server.shared.encrypt :as c]

            ;; Since this will slowly be changing to an api, json is coming
            [clojure.data.json :as json]

            ;; Allowing cross-site requests
            [ring.middleware.cors :refer [wrap-cors]]

            ;; For turning on and off asserts
            [clojure.spec :as s]
            )
  (:gen-class))

(log/set-level! :trace)
(log/info "Compile asserts is set to:" s/*compile-asserts*)
(log/info "System property is:" (System/getProperty "clojure.spec.compile-asserts"))

(defn- parse-long
  "Parses a long, returns 0 if fails"
  [^String i]
  (try (Long/parseLong i)
       (catch NumberFormatException e
         (log/error "parse-long: could not parse:" i "Returning 0 instead.")
         0)
       )
  )

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
  ;; Upload a new character, return the character as edn under the :char tag
  (POST "/api/char/new/"
        request
        (let [body (ring.util.request/body-string request)]
          (log/trace "/api/char/new/ body:" body)
          (let [charid
                (-> body
                    ;; Change to a map format with the char edn in :newchar
                    clojure.edn/read-string
                    ;; Get the char edn
                    :newchar
                    ;; Transform to an actual character file
                    clojure.edn/read-string
                    ;; New characters need drawbacks and mutations
                    (cgen/create-character)
                    ;; Save to db, store the id in charid
                    (sl/save-char-to-db)
                    )]
            (-> charid
                (sl/load-char-from-db)
                ;; Add the id from the database to the character
                ;(assoc :char_id charid) ; Should already be done upon loading
                pr-str
                ((fn [s] {:char s}))
                json/write-str
                )
            )
          )
        )
  ;; TODO currently just creates a new character slot each time, need to update the old one?
  ;; Returns the character
  (POST "/api/char/update/"
        request
        (let [c (-> request
                    ring.util.request/body-string
                    ;; Change to a map format with the char edn in :newchar
                    clojure.edn/read-string
                    ;; Get the char edn
                    :newchar
                    ;; Transform to an actual character file
                    clojure.edn/read-string
                    )
              charid (:char_id c)
              ]
          (log/trace "/api/char/update/" "charid:" charid "type:" (type charid))
          ;; Update it
          (if charid
            (do
              (sl/update-char (int charid) c)
              (json/write-str {:status "okay"})
              )
            (json/write-str {:status "error"})
            )
          )
        )
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
    (log/trace "Char sheet params:" params)
    (let [scen (sl/load-fullscen-from-db (Integer/parseInt (:scen_id params)))]
      (println (:hps scen))
      (sprint/html-print-player-sheet scen ((:hps scen) (Integer/parseInt (:p_id params))))))

  ;; Character Creation
  (GET "/api/db/get-societies/" {params :params}
       (log/trace "/api/db/get-societies/")
       (json/write-str (sql/get-society-all))
       )
  (GET "/api/db/get-mutations/" {params :params}
       (log/trace "/api/db/get-mutations/")
       (json/write-str (sql/get-mutation-all))
       )
  (GET "/api/db/get-drawbacks/" {params :params}
       (log/trace "/api/db/get-drawbacks/")
       (json/write-str (sql/get-drawback-all))
       )
  (GET "/api/db/get-skills/" {params :params}
       (log/trace "/api/db/get-skills/")
       (json/write-str (sql/get-skills-all))
       )

  ;; Hacky way to print a player's entire player sheet
  (GET "/player/:scen_id/:p_id/" {{scen_id :scen_id p_id :p_id} :params baseURL :context}
      (let [scen (sl/load-fullscen-from-db (c/int-show (Integer/parseInt scen_id)))]
         (sprint/html-print-player-sheet scen ((:hps scen) (c/int-show (Integer/parseInt p_id))))))
  ;; Prints the minion sheet from a single scenario
  (GET "/minions/:scen_id/" {{scen_id :scen_id} :params baseURL :context}
       (sprint/html-print-optional (sl/load-fullscen-from-db (c/int-show (Integer/parseInt scen_id))) '(:minions)))

  ;; SECTORGEN
  ;; Prints 7 randomly generated sectors
  (GET "/sectorgen/" []
       (secgen/html-print-neighbours 7))

  ;; LIVE
  (GET "/live/new/:scen_id/" {{scen_id :scen_id} :params baseURL :context}
       (lview/new-game baseURL scen_id))
  (GET "/live/view/:uuid/" {{uid :uuid} :params baseURL :context}
       (lview/view-game baseURL uid))
  (GET "/live/view/:uuid/:confirm/" {{uid :uuid confirm :confirm} :params baseURL :context}
       (lview/edit-game baseURL uid confirm))
  (GET "/live/view/:uuid/:confirm/:index/:amount/"
       {{uid :uuid confirm :confirm index :index amount :amount} :params baseURL :context}
       (lview/edit-game baseURL uid confirm index amount))
  (GET "/live/playerview/:guid/:uuid/"
       {{guid :guid uuid :uuid} :params baseURL :context}
       (lview/view-game-player baseURL guid uuid))

  ;; API
  ;; User login/logout
  (GET "/api/user/login/"
       {{:keys [email password]} :params}
       ;(json/write-str {:error "Not Implemented"})) ;TODO
       (json/write-str {:apiKey "testKey" :user_email "email1@email.com" :user_name "testName"})) ;TODO change away from being mocked

  ;; Public endpoints
  (GET "/api/public/:gameUuid/updates/:lastUpdated/"
       {{gameUuid :gameUuid userUuid :userUuid lastUpdated :lastUpdated} :params}
       (json/write-str (lapi/get-updated-public gameUuid (parse-long lastUpdated))))
  (GET "/api/public/updates/"
       {{:keys [gameUuid userUuid lastUpdated]} :params}
       (json/write-str (lapi/get-updated-public gameUuid (parse-long lastUpdated))))
  (GET "/api/public/:gameUuid/indicies/"
       {{gameUuid :gameUuid userUuid :userUuid} :params}
       (json/write-str (lapi/get-indicies gameUuid)))
  (GET "/api/public/:gameUuid/news/" {{gameUuid :gameUuid userUuid :userUuid} :params} (json/write-str (lapi/get-news gameUuid)))
  (GET "/api/public/:gameUuid/cbay/" {{gameUuid :gameUuid userUuid :userUuid} :params} (json/write-str (lapi/get-cbay gameUuid)))
  (GET "/api/public/:gameUuid/access/" {{gameUuid :gameUuid userUuid :userUuid} :params} (json/write-str (lapi/get-current-access gameUuid)))
  (GET "/api/public/:gameUuid/minions/" {{gameUuid :gameUuid userUuid :userUuid} :params} (json/write-str (lapi/get-minions gameUuid)))

  ;; Player endpoints
  ;; New api
  (GET "/api/player/updates/"
       {{:keys [gameUuid userUuid lastUpdated]} :params}
       (json/write-str (lapi/get-updated-player gameUuid userUuid (parse-long lastUpdated))))
  (GET "/api/player/purchaseminion/"
       {{:keys [gameUuid userUuid sgid minionid]} :params}
       (json/write-str (lapi/player-buy-minion gameUuid userUuid sgid minionid)))
  (GET "/api/player/sendaccess/"
       {{:keys [gameUuid userUuid playerto amount]} :params}
       (json/write-str (lapi/player-send-access gameUuid userUuid playerto amount)))
  ;; Old api
  (GET "/api/player/:gameUuid/:userUuid/updates/:lastUpdated/"
       {{gameUuid :gameUuid userUuid :userUuid lastUpdated :lastUpdated} :params}
       (json/write-str (lapi/get-updated-player gameUuid userUuid (parse-long lastUpdated))))
  (GET "/api/player/:gameUuid/:userUuid/charsheet/"
       {{gameUuid :gameUuid userUuid :userUuid} :params}
       (json/write-str (lapi/get-player-character-sheet gameUuid userUuid)))
  (GET "/api/player/:gameUuid/:userUuid/societymissions/"
       {{gameUuid :gameUuid userUuid :userUuid} :params}
       (json/write-str (lapi/get-player-society-missions gameUuid userUuid)))
  (GET "/api/player/:gameUuid/:userUuid/minions/"
       {{gameUuid :gameUuid userUuid :userUuid} :params}
       (json/write-str (lapi/get-minions gameUuid userUuid)))

  ;; Admin endpoints
  ;; New api
  (GET "/api/admin/updates/"
       {{:keys [gameUuid userUuid lastUpdated]} :params}
       (json/write-str (lapi/get-updated-admin gameUuid userUuid (parse-long lastUpdated))))
  (GET "/api/admin/set-sg-owner/"
       {{:keys [gameUuid userUuid sgid newOwner]} :params}
       (json/write-str (lapi/admin-set-sg-owner gameUuid userUuid sgid newOwner)))
  (GET "/api/admin/modify-index/"
       {{:keys [gameUuid userUuid ind amount]} :params}
       (lapi/admin-modify-index gameUuid userUuid ind amount))
  (GET "/api/admin/modify-access/"
       {{:keys [gameUuid userUuid player amount]} :params}
       (lapi/admin-modify-access gameUuid userUuid player amount))
  ;; Old api
  (GET "/api/admin/:gameUuid/:userUuid/debug/"
       {{gameUuid :gameUuid userUuid :userUuid} :params}
       (lapi/admin-debug gameUuid userUuid))
  (GET "/api/admin/:gameUuid/:userUuid/valid/"
       {{gameUuid :gameUuid userUuid :userUuid} :params}
       (lapi/admin-validate-spec gameUuid userUuid))
  (GET "/api/admin/:gameUuid/:userUuid/modify-index/:ind/:amount/"
       {{gameUuid :gameUuid userUuid :userUuid ind :ind amount :amount} :params}
       (lapi/admin-modify-index gameUuid userUuid ind amount))
  (GET "/api/admin/:gameUuid/:userUuid/minions/"
       {{gameUuid :gameUuid userUuid :userUuid} :params}
       (json/write-str (lapi/get-minions gameUuid userUuid)))
  (GET "/api/admin/:gameUuid/:userUuid/set-sg-owner/:sgid/:new-owner/"
       {{gameUuid :gameUuid userUuid :userUuid sgid :sgid newOwner :new-owner} :params}
       (json/write-str (lapi/admin-set-sg-owner gameUuid userUuid sgid newOwner)))

  ;; OTHER
  ;; Simple directs to the above
  (GET "/" {baseURL :context}
    (html [:html
           [:body
            [:a {:href (str baseURL "/scen/")} "Scenario Generator"][:br]
            [:a {:href (str baseURL "/char/")} "Character Generator"][:br]
            [:a {:href (str baseURL "/sectorgen/")} "Sector Generator"][:br]
            [:br]
            [:a {:href "https://github.com/lsenjov/hphelper"} "Source Code"][:br]
            ]]))

  ;; Turn on or off trace logging
  (GET "/admin/tracking/trace/" [] (do (log/set-level! :trace) "Logging set to trace."))
  (GET "/admin/tracking/info/" [] (do (log/set-level! :info) "Logging set to info."))
  (GET "/admin/spec/on/" [] (do (s/check-asserts true) "Asserts turned on."))
  (GET "/admin/spec/off/" [] (do (s/check-asserts false) "Asserts turned off."))
  (route/resources "/")
  (route/not-found
    (json/write-str {:status "error" :message "Invalid endpoint"}))
  )

(def app
  (-> app-routes
      (wrap-defaults api-defaults)
      ring.middleware.session/wrap-session
      ;; Allows cross-site requests for development purposes. Eventually will remove
      ;(wrap-cors #".*")
      )
  )
