(ns route-ccrs-web.transit.handlers
  (:require [cognitect.transit :as tr]
            [clj-time.format :as tf]))

(def date-formatter (:date tf/formatters))

(def write-handlers
  {org.joda.time.LocalDate
   (tr/write-handler
     (fn [d] "Date")
     (fn [d] (tf/unparse-local-date date-formatter d)))})

(def read-handlers
  {"Date"
   (tr/read-handler
     (fn [d] (tf/parse-local-date d)))})
