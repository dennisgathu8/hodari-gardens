(ns hodari-gardens.core
  "Main entry point for Hodari Gardens ClojureScript application.
   Initializes Re-frame, routing, and mounts the root component."
  (:require [reagent.dom.client :as rdom]
            [re-frame.core :as rf]
            [hodari-gardens.events]
            [hodari-gardens.subs]
            [hodari-gardens.views :as views]
            [hodari-gardens.routes :as routes]))

(defonce root (atom nil))

(defn mount-root
  "Mount the root React component to the DOM using React 18 API."
  []
  (rf/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (when root-el
      (if @root
        (rdom/render @root [views/main-panel])
        (let [new-root (rdom/create-root root-el)]
          (reset! root new-root)
          (rdom/render new-root [views/main-panel]))))))

(defn ^:export init
  "Application initialization function.
   Called once when the app loads."
  []
  (rf/dispatch-sync [:initialize-db])
  (routes/init-routes!)
  (mount-root))
