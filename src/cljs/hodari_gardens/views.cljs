(ns hodari-gardens.views
  "Main view components for Hodari Gardens application.
   Contains all UI components organized by section."
  (:require [re-frame.core :as rf]
            [hodari-gardens.routes :as routes]
            [hodari-gardens.components.hero :as hero]
            [hodari-gardens.components.navigation :as nav]
            [hodari-gardens.components.accommodation :as accommodation]
            [hodari-gardens.components.drinks :as drinks]
            [hodari-gardens.components.events :as events]
            [hodari-gardens.components.worldcup :as worldcup]
            [hodari-gardens.components.contact :as contact]
            [hodari-gardens.components.footer :as footer]
            [hodari-gardens.components.about :as about]))

(defn home-page
  "Home page with hero and overview sections."
  []
  [:div
   [hero/hero-section]
   [accommodation/accommodation-preview]
   [drinks/drinks-preview]
   [events/events-preview]
   [worldcup/worldcup-preview]])

(defn accommodation-page
  "Full accommodation page."
  []
  [accommodation/accommodation-section])

(defn drinks-page
  "Full drinks & dining page."
  []
  [drinks/drinks-section])

(defn events-page
  "Full events & gardens page."
  []
  [events/events-section])

(defn worldcup-page
  "Full World Cup 2026 page."
  []
  [worldcup/worldcup-section])

(defn contact-page
  "Contact page."
  []
  [contact/contact-section])

(defn about-page
  "About page."
  []
  [about/about-section])

(defn current-page
  "Render current page based on route."
  []
  (let [route @(rf/subscribe [:current-route])]
    (case route
      :home [home-page]
      :accommodation [accommodation-page]
      :drinks [drinks-page]
      :events [events-page]
      :worldcup [worldcup-page]
      :contact [contact-page]
      :about [about-page]
      [home-page])))

(defn main-panel
  "Main application panel with navigation and content."
  []
  [:div.min-h-screen.flex.flex-col
   [nav/navigation]
   [:main.flex-grow
    [current-page]]
   [footer/footer]])
