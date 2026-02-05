(ns hodari-gardens.components.worldcup
  "World Cup 2026 section components with countdown and match schedule."
  (:require [re-frame.core :as rf]
            [hodari-gardens.routes :as routes]))

(defn countdown-timer
  "Countdown timer to World Cup 2026."
  []
  (let [tournament @(rf/subscribe [:worldcup-tournament])
        start-date (:start-date tournament)]
    [:div.bg-gradient-to-r.from-blue-600.to-red-600.text-white.p-8.rounded-xl.text-center
     [:h3.text-3xl.font-bold.mb-4 "FIFA World Cup 2026"]
     [:div.text-5xl.font-bold.mb-2 "⚽"]
     [:p.text-xl.mb-4 "Countdown to Kickoff"]
     [:div.grid.grid-cols-4.gap-4.max-w-md.mx-auto
      [:div.bg-white.bg-opacity-20.rounded-lg.p-4
       [:div.text-3xl.font-bold "120"]
       [:div.text-sm "Days"]]
      [:div.bg-white.bg-opacity-20.rounded-lg.p-4
       [:div.text-3xl.font-bold "15"]
       [:div.text-sm "Hours"]]
      [:div.bg-white.bg-opacity-20.rounded-lg.p-4
       [:div.text-3xl.font-bold "30"]
       [:div.text-sm "Minutes"]]
      [:div.bg-white.bg-opacity-20.rounded-lg.p-4
       [:div.text-3xl.font-bold "45"]
       [:div.text-sm "Seconds"]]]
     [:p.mt-4.text-lg
      "June 11 - July 19, 2026"]]))

(defn match-card
  "Individual match card."
  [match]
  [:div.card.p-4
   {:class (when (:featured match) "ring-2 ring-blue-500")}
   (when (:featured match)
     [:div.bg-blue-500.text-white.px-2.py-1.rounded.text-xs.font-semibold.inline-block.mb-2
      "Featured"])
   [:div.text-sm.text-gray-600.dark:text-gray-400.mb-2
    (str (name (:stage match)) " - Group " (:group match))]
   [:div.flex.justify-between.items-center.mb-3
    [:div.text-center.flex-1
     [:div.font-bold.text-lg.text-gray-900.dark:text-white
      (:home-team match)]]
    [:div.text-2xl.font-bold.text-gray-400.px-4 "VS"]
    [:div.text-center.flex-1
     [:div.font-bold.text-lg.text-gray-900.dark:text-white
      (:away-team match)]]]
   [:div.text-sm.text-gray-600.dark:text-gray-400.text-center
    [:div (:date match)]
    [:div (:time match)]
    [:div.text-xs.mt-1 (:venue match)]]])

(defn viewing-package-card
  "Viewing package card."
  [package]
  [:div.card.p-6
   {:class (when (:popular package) "ring-2 ring-resort-gold-500")}
   (when (:popular package)
     [:div.bg-resort-gold-500.text-white.px-3.py-1.rounded-full.text-sm.font-semibold.inline-block.mb-4
      "Popular"])
   [:h3.text-xl.font-bold.mb-2.text-gray-900.dark:text-white
    (:name package)]
   [:p.text-gray-600.dark:text-gray-300.mb-4.text-sm
    (:description package)]
   [:div.text-3xl.font-bold.text-blue-600.dark:text-blue-400.mb-4
    "KSh " (get-in package [:price :ksh])]
   [:ul.space-y-2.text-sm.text-gray-600.dark:text-gray-300.mb-4
    (for [item (:includes package)]
      ^{:key item}
      [:li.flex.items-start
       [:svg.w-4.h-4.mr-2.mt-0.5.text-blue-600.flex-shrink-0 {:xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 20 20" :fill "currentColor"}
        [:path {:fill-rule "evenodd" :d "M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" :clip-rule "evenodd"}]]
       item])]
   [:a.btn-primary.w-full.text-center.block
    {:href (routes/href :contact)}
    "Reserve Now"]])

(defn worldcup-preview
  "Preview section for home page."
  []
  (rf/dispatch [:fetch-worldcup])
  (fn []
    (let [worldcup @(rf/subscribe [:worldcup])
          featured-matches (take 3 (filter :featured (:matches worldcup)))]
      [:section#worldcup.py-16.bg-white.dark:bg-gray-800
       [:div.max-w-7xl.mx-auto.px-4.sm:px-6.lg:px-8
        [:div.text-center.mb-12
         [:h2.section-title "World Cup 2026"]
         [:p.section-subtitle
          "Watch every match on our big screens with the best atmosphere!"]]
        [countdown-timer]
        [:div.mt-12
         [:h3.text-2xl.font-bold.mb-6.text-center.text-gray-900.dark:text-white
          "Featured Matches"]
         [:div.grid.grid-cols-1.md:grid-cols-3.gap-6
          (for [match featured-matches]
            ^{:key (:id match)}
            [match-card match])]]
        [:div.text-center.mt-8
         [:a.btn-outline
          {:href (routes/href :worldcup)}
          "View Full Schedule"]]]])))

(defn worldcup-section
  "Full World Cup section."
  []
  (rf/dispatch [:fetch-worldcup])
  (fn []
    (let [worldcup @(rf/subscribe [:worldcup])
          matches (:matches worldcup)
          packages (:viewing-packages worldcup)
          facilities (:facilities worldcup)]
      [:section.py-16.bg-white.dark:bg-gray-900
       [:div.max-w-7xl.mx-auto.px-4.sm:px-6.lg:px-8
        [:div.text-center.mb-12
         [:h1.section-title "FIFA World Cup 2026"]
         [:p.section-subtitle
          "Experience the world's greatest football tournament with us"]]
        
        ;; Countdown
        [:div.mb-16
         [countdown-timer]]
        
        ;; Viewing Packages
        [:div.mb-16
         [:h2.text-3xl.font-serif.font-bold.mb-8.text-center.text-gray-900.dark:text-white
          "Viewing Packages"]
         [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-4.gap-6
          (for [package packages]
            ^{:key (:id package)}
            [viewing-package-card package])]]
        
        ;; Match Schedule
        [:div.mb-16
         [:h2.text-3xl.font-serif.font-bold.mb-8.text-center.text-gray-900.dark:text-white
          "Match Schedule"]
         [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-6
          (for [match matches]
            ^{:key (:id match)}
            [match-card match])]]
        
        ;; Facilities
        [:div
         [:h2.text-3xl.font-serif.font-bold.mb-8.text-center.text-gray-900.dark:text-white
          "Our Facilities"]
         [:div.grid.grid-cols-1.md:grid-cols-3.gap-8
          (for [screen (:screens facilities)]
            ^{:key (:location screen)}
            [:div.card.p-6
             [:h3.text-xl.font-bold.mb-3.text-gray-900.dark:text-white
              (:location screen)]
             [:div.space-y-2.text-gray-600.dark:text-gray-300
              [:p [:strong "Screen Size:"] " " (:size screen)]
              [:p [:strong "Capacity:"] " " (:capacity screen) " people"]
              [:div
               [:strong "Features:"]
               [:ul.list-disc.list-inside.mt-1
                (for [feature (:features screen)]
                  ^{:key feature}
                  [:li feature])]]]])]]]])))
