(ns route-ccrs-web.api.routes
  (:require [plumbing.core :refer [defnk]]

            [schema.core :as s]
            [schema.coerce :as coerce :refer [string->boolean]]

            [route-ccrs.schema.ids :refer [PartNo]]
            [route-ccrs.schema.parts :refer [Part]]
            [route-ccrs.part-store :as ps]
            [route-ccrs.best-end-dates.calculator :as dc]
            [route-ccrs.best-end-dates.update :refer :all]))

(defnk $parts$:part-no$GET
  "Retrieve a part record"
  {:responses {200 Part}}
  [[:request [:uri-args part-no :- PartNo]
             [:query-params {with-end-dates :- s/Bool true}]]
   [:resources part-store date-calculator]]
  (let [p (ps/get-part part-store {:id part-no})]
    (if (string->boolean with-end-dates)
      {:body (update-all-best-end-dates-under-part p date-calculator)}
      {:body p})))

(defnk $calculate$POST
  "Calculate the best end dates/CCRs for a part record"
  {:responses {200 Part}}
  [[:request body :- Part]
   [:resources date-calculator]]
  {:body (update-all-best-end-dates-under-part body date-calculator)})
