(ns route-ccrs-web.server
  (:require [com.stuartsierra.component :as component]
            [org.httpkit.server :refer [run-server]]))

(defrecord Server [env api-handler]
  component/Lifecycle
  (start [this]
    (let [port (if-let [pn (get env :port)]
                 (java.lang.Integer/parseInt pn)
                 0)
          s (run-server (:f api-handler) {:port port})]
      (assoc this :instance s :port (:local-port (meta s)))))
  (stop [this]
    (if-let [s (:instance this)]
      (do
        (s)
        (dissoc this :instance :port))
      this)))

(defn server []
  (component/using
    (map->Server {})
    [:env :api-handler]))
