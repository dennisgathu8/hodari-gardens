(ns hodari-gardens.server.middleware
  "Security and Cache Control Ring middleware for Hodari Gardens Resort."
  (:require [malli.core :as m]))

;; Malli schema for security headers map validation
(def SecurityHeaders
  [:map {:closed true}
   ["Strict-Transport-Security" :string]
   ["X-Frame-Options" :string]
   ["X-Content-Type-Options" :string]
   ["Referrer-Policy" :string]
   ["Permissions-Policy" :string]
   ["Content-Security-Policy" :string]])

(def security-headers
  {"Strict-Transport-Security" "max-age=31536000; includeSubDomains; preload"
   "X-Frame-Options" "DENY"
   "X-Content-Type-Options" "nosniff"
   "Referrer-Policy" "strict-origin-when-cross-origin"
   "Permissions-Policy" "geolocation=(), microphone=(), camera=()"
   "Content-Security-Policy" "default-src 'self'; script-src 'self' https://fonts.googleapis.com; style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; font-src 'self' https://fonts.gstatic.com; img-src 'self' data:; connect-src 'self'; frame-ancestors 'none'; base-uri 'self'; form-action 'self';"})

;; Validate security headers conform to the schema at compile/load time
(when-not (m/validate SecurityHeaders security-headers)
  (throw (ex-info "Security headers config validation failed"
                  {:hodari/error-type :validation-error
                   :invalid-headers security-headers})))

(defn wrap-security-headers
  "Ring middleware that injects HTTP security headers into every response."
  [handler]
  (fn [req]
    (when-let [resp (handler req)]
      (update resp :headers merge security-headers))))

(defn wrap-cache-headers
  "Ring middleware that injects aggressive cache headers for static assets
   matching /js/ or /css/, and no-cache for all other paths."
  [handler]
  (fn [req]
    (when-let [resp (handler req)]
      (let [uri (:uri req)]
        (cond
          (re-find #"^/(js|css)/" uri)
          (assoc-in resp [:headers "Cache-Control"]
                    "public, max-age=31536000, immutable")
          :else
          (assoc-in resp [:headers "Cache-Control"] "no-cache"))))))

(comment
  ;; REPL exploration — evaluate these forms to verify behaviour
  (let [dummy-handler (fn [_] {:status 200 :headers {}})
        wrapped-sec (wrap-security-headers dummy-handler)
        wrapped-cache (wrap-cache-headers dummy-handler)]
    [(wrapped-sec {:uri "/"})
     (wrapped-cache {:uri "/js/app.js"})
     (wrapped-cache {:uri "/index.html"})])
  )
