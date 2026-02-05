(ns hodari-gardens.components.drinks
  "Drinks and dining section components."
  (:require [re-frame.core :as rf]))

(defn drink-item
  "Individual drink menu item."
  [drink]
  [:div.flex.justify-between.items-start.py-3.border-b.border-gray-200.dark:border-gray-700
   [:div.flex-1
    [:h4.font-semibold.text-gray-900.dark:text-white
     (:name drink)]
    [:p.text-sm.text-gray-600.dark:text-gray-400
     (:description drink)]
    (when (:size drink)
      [:p.text-xs.text-gray-500.dark:text-gray-500.mt-1
       (:size drink)])]
   [:div.ml-4.text-right
    [:span.font-bold.text-garden-green-600.dark:text-garden-green-400
     "KSh " (get-in drink [:price :ksh])]]])

(defn menu-category
  "Menu category section."
  [title items]
  [:div.mb-8
   [:h3.text-2xl.font-serif.font-bold.mb-4.text-gray-900.dark:text-white.border-b-2.border-garden-green-600.pb-2
    title]
   [:div.space-y-2
    (for [item items]
      ^{:key (:id item)}
      [drink-item item])]])

(defn happy-hour-card
  "Happy hour information card."
  [happy-hour]
  [:div.bg-resort-gold-50.dark:bg-resort-gold-900.p-6.rounded-lg.border-2.border-resort-gold-400
   [:div.flex.items-center.mb-2
    [:svg.w-6.h-6.text-resort-gold-600.mr-2 {:xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 20 20" :fill "currentColor"}
     [:path {:fill-rule "evenodd" :d "M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z" :clip-rule "evenodd"}]]
    [:h4.font-bold.text-lg.text-resort-gold-900.dark:text-resort-gold-100
     (:day happy-hour)]]
   [:p.text-resort-gold-800.dark:text-resort-gold-200.font-semibold
    (:time happy-hour)]
   [:p.text-resort-gold-700.dark:text-resort-gold-300.mt-2
    (:discount happy-hour)]])

(defn drinks-preview
  "Preview section for home page."
  []
  (rf/dispatch [:fetch-drinks])
  (fn []
    (let [drinks @(rf/subscribe [:drinks])
          cocktails (take 3 (get-in drinks [:menu :cocktails]))
          happy-hours (:happy-hours drinks)]
      [:section#drinks.py-16.bg-white.dark:bg-gray-800
       [:div.max-w-7xl.mx-auto.px-4.sm:px-6.lg:px-8
        [:div.text-center.mb-12
         [:h2.section-title "Drinks & Dining"]
         [:p.section-subtitle
          "Refresh yourself with our selection of drinks and beverages"]]
        [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8
         [:div
          [:h3.text-2xl.font-serif.font-bold.mb-4.text-gray-900.dark:text-white
           "Featured Cocktails"]
          [:div.space-y-2
           (for [drink cocktails]
             ^{:key (:id drink)}
             [drink-item drink])]]
         [:div
          [:h3.text-2xl.font-serif.font-bold.mb-4.text-gray-900.dark:text-white
           "Happy Hours"]
          [:div.space-y-4
           (for [hh happy-hours]
             ^{:key (:day hh)}
             [happy-hour-card hh])]]]
        [:div.text-center.mt-8
         [:a.btn-primary
          {:href "#drinks"}
          "View Full Menu"]]]])))

(defn drinks-section
  "Full drinks and dining section."
  []
  (rf/dispatch [:fetch-drinks])
  (fn []
    (let [drinks @(rf/subscribe [:drinks])
          menu (:menu drinks)
          happy-hours (:happy-hours drinks)]
      [:section.py-16.bg-white.dark:bg-gray-900
       [:div.max-w-7xl.mx-auto.px-4.sm:px-6.lg:px-8
        [:div.text-center.mb-12
         [:h1.section-title "Drinks & Dining Menu"]
         [:p.section-subtitle
          "Enjoy our carefully curated selection of beverages"]]
        
        ;; Happy Hours
        [:div.mb-12
         [:h2.text-3xl.font-serif.font-bold.mb-6.text-center.text-gray-900.dark:text-white
          "Happy Hours"]
         [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
          (for [hh happy-hours]
            ^{:key (:day hh)}
            [happy-hour-card hh])]]
        
        ;; Menu Categories
        [:div.grid.grid-cols-1.lg:grid-cols-2.gap-12
         [:div
          [menu-category "Cocktails" (:cocktails menu)]
          [menu-category "Wines" (:wines menu)]]
         [:div
          [menu-category "Beers" (:beers menu)]
          [menu-category "Soft Drinks & More" (:soft-drinks menu)]]]]])))
