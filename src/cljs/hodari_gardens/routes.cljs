(ns hodari-gardens.routes
  "Client-side routing for Hodari Gardens SPA.
   Uses Bidi for route matching and Pushy for HTML5 history."
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as rf]))

(def routes
  "Application route definitions."
  ["/" [["" :home]
        ["accommodation" :accommodation]
        ["drinks" :drinks]
        ["events" :events]
        ["worldcup" :worldcup]
        ["contact" :contact]
        ["about" :about]]])

(defn- parse-url
  "Parse URL and extract route information."
  [url]
  (bidi/match-route routes url))

(defn- dispatch-route
  "Dispatch route change to Re-frame."
  [matched-route]
  (let [route-name (:handler matched-route)]
    (rf/dispatch [:set-current-route route-name])))

(defonce history
  (pushy/pushy dispatch-route parse-url))

(defn navigate!
  "Navigate to a route by name."
  [route-name]
  (pushy/set-token! history (bidi/path-for routes route-name)))

(defn init-routes!
  "Initialize routing system."
  []
  (pushy/start! history))

(defn href
  "Generate href for a route."
  [route-name]
  (bidi/path-for routes route-name))
