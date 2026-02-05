(ns hodari-gardens.components.accommodation
  "Accommodation section components."
  (:require [re-frame.core :as rf]
            [hodari-gardens.routes :as routes]))

(defn room-card
  "Individual room card component."
  [room]
  [:div.card.p-6
   [:div.aspect-w-16.aspect-h-9.mb-4.bg-gray-200.dark:bg-gray-700.rounded-lg.overflow-hidden
    [:div.flex.items-center.justify-center.text-gray-400
     "Room Image"]]
   [:h3.text-2xl.font-serif.font-bold.mb-2.text-gray-900.dark:text-white
    (:name room)]
   [:p.text-gray-600.dark:text-gray-300.mb-4
    (:description room)]
   [:div.flex.items-center.justify-between.mb-4
    [:div
     [:span.text-3xl.font-bold.text-garden-green-600.dark:text-garden-green-400
      "KSh " (get-in room [:price :ksh])]
     [:span.text-gray-500.dark:text-gray-400.ml-2
      "/ night"]]
    [:div.text-sm.text-gray-600.dark:text-gray-400
     "Capacity: " (:capacity room) " guests"]]
   [:div.mb-4
    [:h4.font-semibold.mb-2.text-gray-900.dark:text-white "Amenities:"]
    [:ul.grid.grid-cols-2.gap-2.text-sm.text-gray-600.dark:text-gray-300
     (for [amenity (:amenities room)]
       ^{:key amenity}
       [:li.flex.items-center
        [:svg.w-4.h-4.mr-2.text-garden-green-600 {:xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 20 20" :fill "currentColor"}
         [:path {:fill-rule "evenodd" :d "M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" :clip-rule "evenodd"}]]
        amenity])]]
   [:a.btn-primary.w-full.text-center.block
    {:href (routes/href :contact)}
    "Book Now"]])

(defn accommodation-preview
  "Preview section for home page."
  []
  (rf/dispatch [:fetch-rooms])
  (fn []
    (let [rooms @(rf/subscribe [:rooms])
          featured-rooms (take 3 rooms)]
      [:section#accommodation.py-16.bg-gray-50.dark:bg-gray-900
       [:div.max-w-7xl.mx-auto.px-4.sm:px-6.lg:px-8
        [:div.text-center.mb-12
         [:h2.section-title "Luxury Accommodation"]
         [:p.section-subtitle
          "Experience comfort and elegance in our well-appointed rooms"]]
        [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-8
         (for [room featured-rooms]
           ^{:key (:id room)}
           [room-card room])]
        [:div.text-center.mt-8
         [:a.btn-outline
          {:href (routes/href :accommodation)}
          "View All Rooms"]]]])))

(defn accommodation-section
  "Full accommodation section."
  []
  (rf/dispatch [:fetch-rooms])
  (fn []
    (let [rooms @(rf/subscribe [:rooms])]
      [:section.py-16.bg-white.dark:bg-gray-900
       [:div.max-w-7xl.mx-auto.px-4.sm:px-6.lg:px-8
        [:div.text-center.mb-12
         [:h1.section-title "Our Accommodation"]
         [:p.section-subtitle
          "Choose from our range of comfortable rooms designed for your perfect stay"]]
        [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-8
         (for [room rooms]
           ^{:key (:id room)}
           [room-card room])]]])))
