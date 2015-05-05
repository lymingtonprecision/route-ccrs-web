(ns route-ccrs-web.transit.handlers
  (:require [cognitect.transit :as tr]
            [clj-time.format :as tf]
            [schema.utils :as s]))

(def date-formatter (:date tf/formatters))

(def write-handlers
  {org.joda.time.LocalDate
   (tr/write-handler
     (fn [d] "Date")
     (fn [d] (tf/unparse-local-date date-formatter d)))
   schema.utils.ValidationError
   (tr/write-handler
     (fn [e] "ValidationError")
     (fn [e] (s/validation-error-explain e)))
   schema.utils.NamedError
   (tr/write-handler
     (fn [e] "NamedError")
     (fn [e] (s/named-error-explain e)))
   schema.utils.ErrorContainer
   (tr/write-handler
     (fn [e] "SchemaError")
     (fn [e] (s/error-val e)))})

(def read-handlers
  {"Date"
   (tr/read-handler
     (fn [d] (tf/parse-local-date d)))})
