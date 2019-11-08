(ns hphelper.server.websockets
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [taoensso.timbre :as log]
            [clojure.data.json :as json]

            ;; Web sockets
            [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer (get-sch-adapter)]
            )
  (:gen-class))

(let [{:keys [ch-recv send-fn connected-uids
              ajax-post-fn ajax-get-or-ws-handshake-fn]}
      (sente/make-channel-socket! (get-sch-adapter) {})]

  (def ring-ajax-post                ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-chsk                       ch-recv) ; ChannelSocket's receive channel
  (def chsk-send!                    send-fn) ; ChannelSocket's send API fn
  (def connected-uids                connected-uids) ; Watchable, read-only atom
  )

(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  :id ; Dispatch on event-id
  )

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg :keys [id ?data event]}]
  (log/trace "Websocket event:" ev-msg)
  ;(-event-msg-handler ev-msg) ; Handle event-msgs on a single thread
  (future (-event-msg-handler ev-msg)) ; Handle event-msgs on a thread pool
  )

(defonce router_ (atom nil))
(defn  stop-router! [] (when-let [stop-fn @router_] (stop-fn)))
(defn start-router! []
  (stop-router!)
  (reset! router_
    (sente/start-server-chsk-router!
      ch-chsk event-msg-handler)))
(start-router!)
(comment
  (stop-router!)
  (start-router!)
  )

(defroutes ws-routes
  (GET "/" req (ring-ajax-get-or-ws-handshake req))
  (POST "/" req (ring-ajax-post req))
  )


(defmethod -event-msg-handler :default
  [& args]
  (log/trace "Catch as catch can." args)
  )
