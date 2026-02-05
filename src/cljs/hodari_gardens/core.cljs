(ns hodari-gardens.core
  "Main entry point for Hodari Gardens ClojureScript application.
   Initializes Re-frame, routing, and mounts the root component."
  (:require [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [hodari-gardens.events]
            [hodari-gardens.subs]
            [hodari-gardens.views :as views]
            [hodari-gardens.routes :as routes]))

(defn mount-root
  "Mount the root React component to the DOM."
  []
  (rf/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (when root-el
      (rdom/render [views/main-panel] root-el))))

(defn ^:export init
  "Application initialization function.
   Called once when the app loads."
  []
  (rf/dispatch-sync [:initialize-db])
  (routes/init-routes!)
  (mount-root))
