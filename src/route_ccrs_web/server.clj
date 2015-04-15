(ns route-ccrs-web.server
  (:require [com.stuartsierra.component :as component]
            [org.httpkit.server :refer [run-server]]))

(defrecord Server [env api-handler]
  component/Lifecycle
  (start [this]
    (let [s (run-server (:f api-handler) {:port (get env :port 0)})]
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
