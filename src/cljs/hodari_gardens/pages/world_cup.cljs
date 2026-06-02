(ns hodari-gardens.pages.world-cup
  "World Cup 2026 page."
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [clojure.string :as str]))

;; ── Countdown ──────────────────────────────────────────────

(defn- countdown-block [value label]
  [:div.flex.flex-col.items-center.bg-white.bg-opacity-20.rounded-lg.px-5.py-3.min-w-16
   [:span.text-3xl.font-bold.text-white.leading-none (str value)]
   [:span.text-xs.uppercase.tracking-wider.text-white.text-opacity-70.mt-1 label]])

(defn- countdown-section []
  (let [now    @(rf/subscribe [:current-time])
        target (js/Date. "2026-06-11T19:00:00Z")
        diff   (if now (- (.getTime target) (.getTime now)) 0)
        d      (Math/floor (/ diff 86400000))
        h      (Math/floor (/ (mod diff 86400000) 3600000))
        m      (Math/floor (/ (mod diff 3600000) 60000))
        s      (Math/floor (/ (mod diff 60000) 1000))]
    [:div.max-w-3xl.mx-auto.mb-12
     [:div.rounded-2xl.p-8.text-center.shadow-xl.relative.overflow-hidden
      {:style {:background "linear-gradient(135deg, #1d4ed8 0%, #4338ca 50%, #dc2626 100%)"}}
      ;; soccer ball watermark
      [:div.absolute.inset-0.flex.items-center.justify-center.pointer-events-none.opacity-10
       [:div {:style {:font-size "14rem"}} "⚽"]]
      [:div.relative.z-10
       [:h3.text-xl.font-bold.text-white.mb-1 "FIFA World Cup 2026"]
       [:div.text-4xl.mb-2 "⚽"]
       [:p.text-sm.text-white.text-opacity-80.uppercase.tracking-widest.mb-6 "Countdown to Kickoff!"]
       [:div.flex.flex-wrap.gap-3.justify-center.mb-6
        (if (and now (pos? diff))
          [:<>
           [countdown-block d "Days"]
           [countdown-block h "Hrs"]
           [countdown-block m "Min"]
           [countdown-block s "Sec"]]
          [:span.text-2xl.font-bold.text-white.italic "The Tournament is Live!"])]
       [:p.text-base.font-semibold.text-white.tracking-wide "June 11 – July 19, 2026"]]]]))

;; ── Match Card ─────────────────────────────────────────────

(defn- match-card [match]
  (let [parts    (str/split (:match match) #" vs ")
        home     (first parts)
        away     (second parts)
        d        (js/Date. (:date match))
        date-str (.toLocaleDateString d "en-KE"
                   #js {:weekday "short" :day "numeric" :month "short" :year "numeric"
                        :timeZone "Africa/Nairobi"})
        time-str (.toLocaleTimeString d "en-KE"
                   #js {:hour "2-digit" :minute "2-digit" :hour12 false
                        :timeZone "Africa/Nairobi"})
        confirmed? (= "Confirmed" (:status match))]
    [:div.bg-white.dark:bg-gray-800.rounded-xl.p-5.shadow-md.border.border-gray-200.dark:border-gray-700.hover:shadow-lg.transition-shadow.duration-200
     ;; Badge + Group
     [:div.flex.items-center.justify-between.mb-3
      [:span.text-xs.font-bold.uppercase.tracking-wider.text-gray-400 (:group match)]
      [:span.text-xs.font-bold.px-2.py-0.5.rounded-full
       {:class (if confirmed?
                 "bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400"
                 "bg-yellow-100 text-yellow-700 dark:bg-yellow-900/30 dark:text-yellow-400")}
       (:status match)]]
     ;; Teams with flags
     [:div.flex.items-center.justify-center.gap-3.mb-4
      [:div.flex.items-center.gap-2.flex-1.justify-end
       [:span.text-sm.font-bold.text-gray-800.dark:text-gray-200.text-right.truncate home]
       [:span.text-2xl (:home-flag match)]]
      [:span.text-xs.font-bold.text-gray-400.px-2 "VS"]
      [:div.flex.items-center.gap-2.flex-1
       [:span.text-2xl (:away-flag match)]
       [:span.text-sm.font-bold.text-gray-800.dark:text-gray-200.truncate away]]]
     ;; Date / Time / Venue
     [:div.text-center.pt-3.border-t.border-gray-100.dark:border-gray-700
      [:p.text-xs.font-semibold.text-gray-500.dark:text-gray-400.mb-0.5 date-str]
      [:p.text-lg.font-bold.text-gray-900.dark:text-white time-str]
      [:p.text-xs.text-gray-400.dark:text-gray-500.mt-1 (:venue match)]]]))

;; ── Group Section ──────────────────────────────────────────

(defn- group-card [group]
  [:div.bg-white.dark:bg-gray-800.rounded-xl.p-5.shadow-md.border.border-gray-200.dark:border-gray-700
   [:h3.text-lg.font-bold.text-gray-900.dark:text-white.mb-4.border-b.pb-2.flex.items-center.justify-between
    (str "Group " (:letter group))
    [:span.text-xs.font-normal.text-gray-500 "UEFA/CAF/CONMEBOL"]]
   [:div.space-y-3.py-1
    (for [[idx team] (map-indexed vector (:teams group))]
      ^{:key (:name team)}
      [:div.flex.items-center.justify-between
       [:div.flex.items-center.gap-3
        [:span.text-xs.font-semibold.text-gray-400.w-4 (str (inc idx) ".")]
        [:span.text-xl (:flag team)]
        [:span.text-sm.font-medium.text-gray-800.dark:text-gray-200 (:name team)]]
       [:span.text-xs.text-green-600.dark:text-green-400.font-semibold "Live"]])]])

;; ── Venue Section ──────────────────────────────────────────

(defn- venue-card [venue]
  [:div.bg-white.dark:bg-gray-800.rounded-xl.p-5.shadow-md.border.border-gray-200.dark:border-gray-700.flex.flex-col.justify-between
   [:div
    [:div.flex.items-center.justify-between.mb-2
     [:h3.text-base.font-bold.text-gray-900.dark:text-white.truncate (:stadium venue)]
     [:span.text-lg (:flag venue)]]
    [:p.text-xs.text-gray-500.dark:text-gray-400.mb-3 (str (:city venue) ", " (:country venue))]]
   [:div.pt-3.border-t.border-gray-100.dark:border-gray-700.flex.items-center.justify-between
    [:span.text-xs.text-gray-400 "Capacity:"]
    [:span.text-sm.font-bold.text-gray-800.dark:text-gray-200 (.toLocaleString (:capacity venue))]]])

;; ── Knockout timeline ──────────────────────────────────────

(defn- stage-timeline-card [idx stage]
  [:div.relative.pl-8.pb-8.last:pb-0
   ;; timeline line connection
   [:div.absolute.left-3.top-6.bottom-0.w-0.5.bg-gray-200.dark:bg-gray-700.last:hidden]
   ;; timeline node
   [:div.absolute.left-0.top-1.w-6.h-6.rounded-full.border-2.border-indigo-600.bg-white.dark:bg-gray-900.flex.items-center.justify-center.z-10
    [:span.text-xs.font-bold.text-indigo-600 (str idx)]]
   [:div.bg-white.dark:bg-gray-800.rounded-xl.p-5.shadow-md.border.border-gray-200.dark:border-gray-700
    [:div.flex.flex-wrap.items-center.justify-between.gap-2.mb-2
     [:h3.text-lg.font-bold.text-gray-900.dark:text-white (:stage stage)]
     [:span {:class "text-xs font-semibold px-2 py-1 rounded-full bg-indigo-100 text-indigo-700 dark:bg-indigo-900/30 dark:text-indigo-400"} (:dates stage)]]
    [:p.text-sm.text-gray-600.dark:text-gray-300 (:description stage)]]])

;; ── Main Page ──────────────────────────────────────────────

(defn world-cup-page []
  (let [data     @(rf/subscribe [:world-cup])
        active-tab (r/atom :fixtures)]
    (fn []
      (let [fixtures (:fixtures data)
            groups   (:groups data)
            venues   (:venues data)
            stages   (:knockout-stages data)]
        [:div.min-h-screen.bg-gray-50.dark:bg-gray-900
         ;; Header
         [:header.pt-20.pb-6.text-center.bg-white.dark:bg-gray-800.border-b.border-gray-200.dark:border-gray-700
          [:h1.text-4xl.md:text-5xl.font-serif.font-bold.text-gray-900.dark:text-white.mb-2
           "FIFA World Cup 2026 Live Center"]
          [:p.text-lg.text-gray-500.dark:text-gray-400
           "Join us in Nakuru to watch the world's finest clash live!"]]

         [:div.max-w-6xl.mx-auto.px-4.py-10
          ;; Countdown
          [countdown-section]

          ;; Tab Navigation Buttons
          [:div.flex.flex-wrap.gap-2.justify-center.mb-10.bg-gray-100.dark:bg-gray-800.p-1.5.rounded-xl.max-w-xl.mx-auto.shadow-inner
           (for [[tab-key label icon] [[:fixtures "Schedule" "📅"]
                                       [:groups "Groups" "🏆"]
                                       [:venues "Venues" "🏟"]
                                       [:knockout "Knockout" "⚡"]]]
             ^{:key tab-key}
             [:button.px-4.py-2.rounded-lg.text-sm.font-bold.transition-all.duration-200.flex.items-center.gap-2
              {:class (if (= @active-tab tab-key)
                        "bg-white dark:bg-gray-700 text-gray-900 dark:text-white shadow-md"
                        "text-gray-500 hover:text-gray-900 dark:text-gray-400 dark:hover:text-white")
               :on-click #(reset! active-tab tab-key)}
              [:span icon] label])]

          ;; Tab Contents
          (case @active-tab
            :fixtures
            [:div
             [:div.mb-8.text-center
              [:h2.text-2xl.font-serif.font-bold.text-gray-900.dark:text-white.mb-1 "Premium Fixtures Schedule"]
              [:p.text-sm.text-gray-400.dark:text-gray-500 "All times shown in Nairobi Time (EAT)"]]
             [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-5
              (for [fixture fixtures]
                ^{:key (:match fixture)}
                [match-card fixture])]]

            :groups
            [:div
             [:div.mb-8.text-center
              [:h2.text-2xl.font-serif.font-bold.text-gray-900.dark:text-white.mb-1 "Tournament Groups (A–L)"]
              [:p.text-sm.text-gray-400.dark:text-gray-500 "Fully qualified 48-team composition"]]
             [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-5
              (for [group groups]
                ^{:key (:letter group)}
                [group-card group])]]

            :venues
            [:div
             [:div.mb-8.text-center
              [:h2.text-2xl.font-serif.font-bold.text-gray-900.dark:text-white.mb-1 "Official Host Cities & Stadiums"]
              [:p.text-sm.text-gray-400.dark:text-gray-500 "16 spectacular venues across the USA, Mexico, and Canada"]]
             [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-5
              (for [venue venues]
                ^{:key (:stadium venue)}
                [venue-card venue])]]

            :knockout
            [:div
             [:div.mb-8.text-center
              [:h2.text-2xl.font-serif.font-bold.text-gray-900.dark:text-white.mb-1 "The Road to the Championship"]
              [:p.text-sm.text-gray-400.dark:text-gray-500 "Knockout phases leading up to the Grand Final on July 19"]]
             [:div.max-w-2xl.mx-auto.mt-6
              (for [[idx stage] (map-indexed vector stages)]
                ^{:key (:stage stage)}
                [stage-timeline-card (inc idx) stage])]])
          ]]))))
