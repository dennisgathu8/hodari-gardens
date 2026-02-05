(ns hodari-gardens.components.events
  "Events and gardens section components."
  (:require [re-frame.core :as rf]
            [hodari-gardens.routes :as routes]))

(defn event-package-card
  "Event package card component."
  [package]
  [:div.card.p-6
   {:class (when (:popular package) "ring-2 ring-resort-gold-500")}
   (when (:popular package)
     [:div.bg-resort-gold-500.text-white.px-3.py-1.rounded-full.text-sm.font-semibold.inline-block.mb-4
      "Popular"])
   [:h3.text-2xl.font-serif.font-bold.mb-2.text-gray-900.dark:text-white
    (:name package)]
   [:p.text-gray-600.dark:text-gray-300.mb-4
    (:description package)]
   [:div.mb-4
    [:div.text-3xl.font-bold.text-garden-green-600.dark:text-garden-green-400
     "KSh " (get-in package [:price :ksh])]
    [:div.text-sm.text-gray-500.dark:text-gray-400
     "Capacity: " (get-in package [:capacity :min]) " - " (get-in package [:capacity :max]) " guests"]]
   [:div.mb-4
    [:h4.font-semibold.mb-2.text-gray-900.dark:text-white "Includes:"]
    [:ul.space-y-1.text-sm.text-gray-600.dark:text-gray-300
     (for [item (:includes package)]
       ^{:key item}
       [:li.flex.items-start
        [:svg.w-4.h-4.mr-2.mt-0.5.text-garden-green-600.flex-shrink-0 {:xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 20 20" :fill "currentColor"}
         [:path {:fill-rule "evenodd" :d "M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" :clip-rule "evenodd"}]]
        item])]]
   [:div.text-sm.text-gray-600.dark:text-gray-400.mb-4
    "Duration: " (:duration package)]
   [:a.btn-primary.w-full.text-center.block
    {:href (routes/href :contact)}
    "Request Quote"]])

(defn gallery-item
  "Gallery image item with lightbox trigger."
  [item index all-images]
  [:div.aspect-w-16.aspect-h-9.bg-gray-200.dark:bg-gray-700.rounded-lg.overflow-hidden.cursor-pointer.hover:opacity-90.transition-opacity
   {:on-click #(rf/dispatch [:open-lightbox all-images index])
    :role "button"
    :tabIndex 0
    :aria-label (str "View " (:alt item) " in lightbox")
    :on-key-press #(when (= (.-key %) "Enter")
                     (rf/dispatch [:open-lightbox all-images index]))}
   [:div.flex.items-center.justify-center.text-gray-400.text-sm
    (:alt item)]])

(defn lightbox
  "Image lightbox component."
  []
  (let [lightbox-data @(rf/subscribe [:lightbox])
        open? (:open? lightbox-data)
        current-image @(rf/subscribe [:lightbox-current-image])]
    (when open?
      [:div.fixed.inset-0.z-50.bg-black.bg-opacity-90.flex.items-center.justify-center
       {:on-click #(rf/dispatch [:close-lightbox])}
       [:button.absolute.top-4.right-4.text-white.text-4xl.hover:text-gray-300
        {:on-click #(rf/dispatch [:close-lightbox])
         :aria-label "Close lightbox"}
        "×"]
       [:button.absolute.left-4.text-white.text-4xl.hover:text-gray-300
        {:on-click #(do (.stopPropagation %)
                        (rf/dispatch [:lightbox-prev]))
         :aria-label "Previous image"}
        "‹"]
       [:button.absolute.right-4.text-white.text-4xl.hover:text-gray-300
        {:on-click #(do (.stopPropagation %)
                        (rf/dispatch [:lightbox-next]))
         :aria-label "Next image"}
        "›"]
       [:div.max-w-4xl.max-h-screen.p-4
        {:on-click #(.stopPropagation %)}
        [:div.bg-gray-300.rounded-lg.p-8.text-center
         [:p.text-gray-700 (:alt current-image)]]]])))

(defn events-preview
  "Preview section for home page."
  []
  (rf/dispatch [:fetch-events])
  (fn []
    (let [events @(rf/subscribe [:events])
          packages (take 3 (:event-packages events))]
      [:section#events.py-16.bg-gray-50.dark:bg-gray-900
       [:div.max-w-7xl.mx-auto.px-4.sm:px-6.lg:px-8
        [:div.text-center.mb-12
         [:h2.section-title "Gardens & Events"]
         [:p.section-subtitle
          "Host your special occasions in our beautiful garden venue"]]
        [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-8
         (for [package packages]
           ^{:key (:id package)}
           [event-package-card package])]
        [:div.text-center.mt-8
         [:a.btn-outline
          {:href (routes/href :events)}
          "View All Packages"]]]])))

(defn events-section
  "Full events section with gallery."
  []
  (rf/dispatch [:fetch-events])
  (fn []
    (let [events @(rf/subscribe [:events])
          packages (:event-packages events)
          gallery (:gallery events)]
      [:section.py-16.bg-white.dark:bg-gray-900
       [:div.max-w-7xl.mx-auto.px-4.sm:px-6.lg:px-8
        [:div.text-center.mb-12
         [:h1.section-title "Events & Gardens"]
         [:p.section-subtitle
          "Create unforgettable memories in our stunning garden venue"]]
        
        ;; Event Packages
        [:div.mb-16
         [:h2.text-3xl.font-serif.font-bold.mb-8.text-center.text-gray-900.dark:text-white
          "Event Packages"]
         [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-8
          (for [package packages]
            ^{:key (:id package)}
            [event-package-card package])]]
        
        ;; Gallery
        [:div
         [:h2.text-3xl.font-serif.font-bold.mb-8.text-center.text-gray-900.dark:text-white
          "Event Gallery"]
         [:div.grid.grid-cols-2.md:grid-cols-3.lg:grid-cols-4.gap-4
          (map-indexed
           (fn [idx item]
             ^{:key (:id item)}
             [gallery-item item idx gallery])
           gallery)]]
        
        ;; Lightbox
        [lightbox]]])))
