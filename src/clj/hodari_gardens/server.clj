(ns hodari-gardens.server
  "Main server namespace for Hodari Gardens Resort backend.
   Handles HTTP routing, API endpoints, and serves static assets."
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.util.response :as response]
            [reitit.ring :as ring]
            [reitit.ring.middleware.parameters :refer [parameters-middleware]]
            [hodari-gardens.api :as api])
  (:gen-class))

(defn index-handler [_]
  "Handler to serve the main index.html file for SPA routing."
  (-> (response/resource-response "public/index.html")
      (response/content-type "text/html")))

(def app-routes
  "Application routes using Reitit router.
   Defines all API endpoints and their handlers."
  [["/api"
    ["/rooms" {:get api/get-rooms}]
    ["/rooms/:id" {:get api/get-room-by-id}]
    ["/events" {:get api/get-events}]
    ["/events/:id" {:get api/get-event-by-id}]
    ["/drinks" {:get api/get-drinks}]
    ["/worldcup" {:get api/get-worldcup-data}]
    ["/worldcup/matches" {:get api/get-matches}]
    ["/contact" {:get api/get-contact-info}]
    ["/booking" {:post api/submit-booking}]
    ["/inquiry" {:post api/submit-inquiry}]]])

(def app
  "Main application handler with middleware stack.
   Wraps routes with JSON, parameters, and default middleware.
   Includes a fallback handler for SPA client-side routing."
  (ring/ring-handler
   (ring/router app-routes
                {:data {:middleware [parameters-middleware]}})
   (ring/routes
    (ring/create-resource-handler {:root "public" :path "/"})
    (ring/create-default-handler
     {:not-found index-handler}))
   {:middleware [[wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false)]
                 wrap-json-response
                 [wrap-json-body {:keywords? true}]]}))

(def dev-app
  "Development application with auto-reload enabled."
  (wrap-reload app))

(defn -main
  "Start the Jetty server on port 3000.
   Use dev-app in development for hot reloading."
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (println (str "Starting Hodari Gardens server on port " port))
    (run-jetty dev-app {:port port :join? false})))

(comment
  ;; REPL development helpers
  (def server (-main))
  (.stop server))
