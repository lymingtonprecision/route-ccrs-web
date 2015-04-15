(ns route-ccrs-web.system
  (:require [com.stuartsierra.component :as component]
            [environ.core :refer (env)]

            [route-ccrs-web.logging :as log]
            [route-ccrs-web.database :as db]
            [route-ccrs-web.api.handler :as api]
            [route-ccrs-web.server :as srv]

            [route-ccrs.part-store :refer [ifs-part-store]]
            [route-ccrs.best-end-dates.calculator :refer [ifs-date-calculator]]))

(defn system
  ([] (system env))
  ([env]
   (component/system-map
     :env env
     :db (db/database)
     :part-store (ifs-part-store)
     :date-calculator (ifs-date-calculator)
     :api-handler (api/api-handler)
     :server (srv/server))))

(defn start [s]
  (log/start!)
  (component/start s))

(defn stop [s]
  (log/stop!)
  (component/stop s))
