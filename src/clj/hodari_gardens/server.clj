(ns hodari-gardens.server
  "Production-ready backend server for Hodari Gardens Resort.
   
   Features:
   - Reitit-powered REST API
   - Re-frame compatible JSON serialization
   - Fallback routing for Single Page Application (SPA)
   - Static resource serving from public/ directory
   - Security hardening (CSRF, Security Headers)"
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :as response]
            [reitit.ring :as ring]
            [reitit.ring.middleware.parameters :refer [parameters-middleware]]
            [hodari-gardens.api :as api]
            [hodari-gardens.layout :as layout]
            [hodari-gardens.server.middleware :refer [wrap-security-headers wrap-cache-headers]]
            [hodari-gardens.server.rate-limit :refer [wrap-rate-limit]])
  (:gen-class))

(defn index-handler
  "Handler to serve the dynamic Hiccup layout.
   Ensures the response is a valid Ring response map."
  [request]
  (-> (layout/render-page request {})
      (response/response)
      (response/content-type "text/html")))

(def app
  "Main application handler with middleware stack."
  (-> (ring/ring-handler
       (ring/router [["/api"
                      {:middleware [wrap-json-response [wrap-json-body {:keywords? true}]]}
                      ["/rooms" {:get api/get-rooms}]
                      ["/rooms/:id" {:get api/get-room-by-id}]
                      ["/events" {:get api/get-events}]
                      ["/events/:id" {:get api/get-event-by-id}]
                      ["/drinks" {:get api/get-drinks}]
                      ["/contact" {:get api/get-contact-info}]
                      ["/world-cup" {:get api/get-world-cup-data}]
                      ["/booking" {:post api/submit-booking}]
                      ["/inquiry" {:post api/submit-inquiry}]]
                     ["/health" {:get (fn [_req]
                                        {:status  200
                                         :headers {"Content-Type" "application/json"}
                                         :body    "{\"status\":\"ok\"}"})}]
                     ["/" {:get index-handler}]
                     ["/index.html" {:get index-handler}]
                     ["/accommodation" {:get index-handler}]
                     ["/drinks" {:get index-handler}]
                     ["/events" {:get index-handler}]
                     ["/contact" {:get index-handler}]
                     ["/about" {:get index-handler}]
                     ["/world-cup" {:get index-handler}]]
                    {:data {:middleware [parameters-middleware]}})
       (ring/routes
        (ring/create-resource-handler {:root "public" :path "/"})
        (ring/create-default-handler
         {:not-found index-handler})))
      wrap-cache-headers
      wrap-rate-limit
      wrap-security-headers
      (wrap-defaults (-> site-defaults
                         (assoc-in [:security :frame-options] false)
                         (assoc-in [:security :content-type-options] false)
                         (assoc-in [:security :xss-protection] false)
                         (assoc :static false)))))

(defn -main
  "Start the Jetty server on port 3000."
  [& _args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (println (str "Starting Hodari Gardens server on port " port " (0.0.0.0)"))
    (run-jetty app {:port port :host "0.0.0.0" :join? false})))

(comment
  ;; REPL development helpers
  (def server (-main))
  (.stop server))

