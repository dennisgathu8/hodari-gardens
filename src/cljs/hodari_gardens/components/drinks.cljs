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
       (:size drink)])]])

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

(defn drinks-preview
  "Preview section for home page."
  []
  (rf/dispatch [:fetch-drinks])
  (fn []
    (let [drinks @(rf/subscribe [:drinks])
          cocktails (take 3 (get-in drinks [:menu :cocktails]))]
      [:section#drinks.py-16.bg-white.dark:bg-gray-800
       [:div.max-w-7xl.mx-auto.px-4.sm:px-6.lg:px-8
        [:div.text-center.mb-12
         [:h2.section-title "Drinks & Dining"]
         [:p.section-subtitle
          "Refresh yourself with our selection of drinks and beverages"]]
         [:div.max-w-3xl.mx-auto
          [:h3.text-2xl.font-serif.font-bold.mb-4.text-gray-900.dark:text-white.text-center
           "Featured Cocktails"]
          [:div.space-y-2
           (for [drink cocktails]
             ^{:key (:id drink)}
             [drink-item drink])]]
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
          menu (:menu drinks)]
      [:section.py-16.bg-white.dark:bg-gray-900
       [:div.max-w-7xl.mx-auto.px-4.sm:px-6.lg:px-8
        [:div.text-center.mb-12
         [:h1.section-title "Drinks & Dining Menu"]
         [:p.section-subtitle
          "Enjoy our carefully curated selection of beverages"]]
        
        ;; Menu Categories
        [:div.grid.grid-cols-1.lg:grid-cols-2.gap-12
         [:div
          [menu-category "Cocktails" (:cocktails menu)]
          [menu-category "Wines" (:wines menu)]]
         [:div
          [menu-category "Beers" (:beers menu)]
          [menu-category "Soft Drinks & More" (:soft-drinks menu)]]]]])))
