(ns route-ccrs-web.api.access-control
  "Provides Ring middleware for dealing with CORS (Cross Origin Requests)
  in a decidedly permissive manner: it just accepts everything.

  This isn't an issue for this service because we don't establish any
  sessions or deal with any user specific or secure data. Just don't
  go copying this middleware elsewhere thinking you'll be secure.

  All you need to do is wrap your handler with `wrap-allow-origin` (or
  add it to your middleware stack) and all incoming CORS requests will
  be accepted auto-magically."
  (:require [clojure.string :refer [replace-first]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Request handling

(defn is-access-control-preflight-request?
  "Returns truthy if the Ring request `req` looks like a CORS preflight
  request."
  [req]
  (and (= (req :request-method) :options)
       (get-in req [:headers "origin"])
       (get-in req [:headers "access-control-request-method"])))

(defn add-allowed-origin
  "Adds the `Access-Control-Allow-Origin` header to the response, `resp`,
  for the `Origin` specified in the request, `req`."
  [req resp]
  (if-let [origin (get-in req [:headers "origin"])]
    (assoc-in resp [:headers "access-control-allow-origin"] origin)
    resp))

(defn access-control-request->access-control-allow
  "Returns the `Access-Control-Allow-*` headers corresponding to the
  `Access-Control-Request-*` headers in the `headers` map."
  [headers]
  (let [acr (select-keys
              headers
              (filter #(re-find #"(?i)^access\-control\-request\-" %)
                      (keys headers)))]
    (reduce
      (fn [r [k v]]
        (assoc r (replace-first k #"\-request\-" "-allow-") v))
      {}
      acr)))

(defn accept-access-control-preflight-req
  "Given a CORS preflight Ring request returns a response that will
  allow the original request to proceed."
  [req]
  (let [aca (access-control-request->access-control-allow (req :headers))
        resp {:status 200
              :headers (merge aca {"content-type" "text/plain"})
              :body "ok"}]
    (add-allowed-origin req resp)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public

(defn wrap-allow-origin
  "Ring middleware that wraps the request handler in a fn that will
  intercept CORS preflight requests, short cutting the request processing
  and accepting the request, and add the `Access-Control-Allow-Origin`
  header to all requests with a specified `Origin`."
  [handler]
  (fn [req]
    (if (is-access-control-preflight-request? req)
      (accept-access-control-preflight-req req)
      (add-allowed-origin req (handler req)))))
