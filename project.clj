(defproject hphelper "0.1.0-SNAPSHOT"
  :description "A suite of tools for the discerning high programmer"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.9.0-alpha10"]
                 ;; Web Server
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-anti-forgery "1.0.0"]

                 ;; Displaying HTML
                 [hiccup "1.0.5"]

                 ;; Database deps
                 [mysql/mysql-connector-java "5.1.38"]
                 [org.clojure/java.jdbc "0.4.2"]

                 ;; Logging Deps
                 [com.taoensso/timbre "4.5.1"]

                 ;; Json Deps
                 [org.clojure/data.json "0.2.6"]
                 ]
  :plugins [[lein-ring "0.9.7"]
            [cider/cider-nrepl "0.10.1"]]
  :ring {:handler hphelper.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
