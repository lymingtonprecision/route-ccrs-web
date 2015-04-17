(ns route-ccrs-web.api.handler
  (:require [com.stuartsierra.component :as component]
            [plumbing.core :refer [keywordize-map]]

            [fnhouse.handlers :as handlers]
            [fnhouse.routes :as routes]

            [ring.middleware.params :as params]
            [ring.middleware.transit :as mwtr]

            [route-ccrs-web.api.routes]
            [route-ccrs-web.api.access-control :refer [wrap-allow-origin]]
            [route-ccrs-web.transit.handlers :as trh]))

(defn wrap-keywordize-params [handler]
  (fn [req]
    (handler (update-in req [:query-params] keywordize-map))))

(defn ring-middleware [handler]
  (-> handler
      wrap-keywordize-params
      (mwtr/wrap-transit-body
        {:opts {:handlers trh/read-handlers}})
      params/wrap-params
      (mwtr/wrap-transit-response
        {:encoding :json :opts {:handlers trh/write-handlers}})
      wrap-allow-origin))

(defrecord ApiHandler [part-store date-calculator]
  component/Lifecycle
  (start [this]
    (let [h-fns (handlers/ns->handler-fns
                  'route-ccrs-web.api.routes
                  (constantly nil))
          ch ((handlers/curry-resources h-fns) this)]
      (assoc this :f (ring-middleware (routes/root-handler ch)))))
  (stop [this]
    (dissoc this :f)))

(defn api-handler []
  (component/using
    (map->ApiHandler {})
    [:part-store :date-calculator]))
