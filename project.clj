(defproject hphelper "0.1.0-SNAPSHOT"
  :description "High Programmer Helper"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"

  :dependencies [;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                 ;; Shared Deps
                 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                 [org.clojure/clojure "1.10.1"]

                 ;; Websockets
                 [com.taoensso/sente "1.14.0"]

                 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                 ;; Server Deps
                 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                 [org.clojure/core.async "0.4.500"
                  :exclusions [org.clojure/tools.reader]]

                 ;; Web Server
                 [compojure "1.6.1"]
                 [ring "1.7.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-anti-forgery "1.3.0"]
                 [ring-logger "1.0.1"]

                 ;; For running an uberjar
                 [http-kit "2.3.0"]

                 ;; Logging Deps
                 [com.taoensso/timbre "4.10.0"]

                 ;; Json Deps
                 [org.clojure/data.json "0.2.6"]

                 ;; Database deps
                 [mysql/mysql-connector-java "8.0.18"]
                 [org.clojure/java.jdbc "0.7.10"]

                 ;; Displaying HTML
                 [hiccup "1.0.5"]

                 ;; Allowing cross-site requests
                 [jumblerg/ring.middleware.cors "1.0.1"]

                 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                 ;; Frontend Deps
                 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                 [org.clojure/clojurescript "1.10.520"]
                 [reagent "0.8.1"]
                 [cljs-ajax "0.8.0"]
                 [com.cemerick/url "0.1.1"]
                 ]

  :plugins [[lein-figwheel "0.5.19"]
            [lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]
            [lein-ring "0.12.5"]
            ]

  ;; Okay, uncommenting this somehow makes the combined builds not work. Don't do it.
  ;:source-paths ["src/hphelper/server" "src/hphelper/shared"]

  :main hphelper.server.handler

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src/hphelper/frontend"]

                ;; the presence of a :figwheel configuration here
                ;; will cause figwheel to inject the figwheel client
                ;; into your build
                :figwheel {:on-jsload "hphelper.frontend.core/on-js-reload"
                           ;; :open-urls will pop open your application
                           ;; in the default browser once Figwheel has
                           ;; started and complied your application.
                           ;; Comment this out once it no longer serves you.
                           ;; Opens on 3000 to go with the ring server
                           :open-urls ["http://localhost:3000/index.html"]
                           }

                :compiler {:main hphelper.frontend.core
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/hphelper.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true
                           ;; To console.log CLJS data-structures make sure you enable devtools in Chrome
                           ;; https://github.com/binaryage/cljs-devtools
                           :preloads [devtools.preload]}}
               ;; This next build is an compressed minified build for
               ;; production. You can build this with:
               ;; lein cljsbuild once min
               {:id "min"
                :source-paths ["src/hphelper/frontend" "src/hphelper/shared"]
                :compiler {:output-to "resources/public/js/compiled/hphelper.js"
                           :main hphelper.frontend.core
                           :optimizations :advanced
                           :pretty-print false}}]}

  :figwheel {;; :http-server-root "public" ;; default and assumes "resources"
             ;; :server-port 3449 ;; default
             ;; :server-ip "127.0.0.1"

             :css-dirs ["resources/public/css"] ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process
             :nrepl-port 7888

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this

             ;; doesn't work for you just run your own server :) (see lein-ring)

             ;; :ring-handler hphelper.server.handler/app

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             ;; if you are using emacsclient you can just use
             ;; :open-file-command "emacsclient"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             ;; :server-logfile "tmp/logs/figwheel-logfile.log"
             }

  :ring {:handler hphelper.server.handler/app
         :uberwar-name "hphelper.war"
         :uberjar-name "hphelper.jar"
         :global-vars {*warn-on-reflection* true}
         }

  ;; setting up nREPL for Figwheel and ClojureScript dev
  ;; Please see:
  ;; https://github.com/bhauman/lein-figwheel/wiki/Using-the-Figwheel-REPL-within-NRepl


  :profiles {:dev {:dependencies [[org.clojure/test.check "0.10.0"]
                                  ]
                   ;; need to add dev source path here to get user.clj loaded
                   :source-paths ["src" "dev"]
                   ;; for CIDER
                   ;; :plugins [[cider/cider-nrepl "0.12.0"]]
                   :repl-options {; for nREPL dev you really need to limit output
                                  :init (set! *print-length* 50)
                                  ;:nrepl-middleware [cider.piggieback/wrap-cljs-repl]
                                  }
                   }
             ;; Specifically remove asserts on compilation
             :prod {:jvm-opts ["-Dclojure.spec.compile-asserts=false"]}
             :uberjar {;:hooks [minify-assets.plugin/hooks]
                       ;:source-paths ["env/prod/clj"]
                       :source-paths ["src/hphelper/server" "src/hphelper/shared"]
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :env {:production true}
                       :aot :all
                       :uberjar-name "hphelper.jar"
                       :main hphelper.server.handler
                       :omit-source true}
             :uberwar {;:hooks [minify-assets.plugin/hooks]
                       ;:source-paths ["env/prod/clj"]
                       :source-paths ["src/hphelper/server" "src/hphelper/shared"]
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :env {:production true}
                       :aot :all
                       :uberwar-name "hphelper.war"
                       :omit-source true}
             }
)
