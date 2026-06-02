(ns hodari-gardens.pages.world-cup
  "World Cup 2026 page."
  (:require [re-frame.core :as rf]
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

;; ── Main Page ──────────────────────────────────────────────

(defn world-cup-page []
  (let [data     @(rf/subscribe [:world-cup])
        fixtures (:fixtures data)]
    [:div.min-h-screen.bg-gray-50.dark:bg-gray-900
     ;; Header
     [:header.pt-20.pb-6.text-center.bg-white.dark:bg-gray-800.border-b.border-gray-200.dark:border-gray-700
      [:h1.text-4xl.md:text-5xl.font-serif.font-bold.text-gray-900.dark:text-white.mb-2
       "FIFA World Cup 2026"]
      [:p.text-lg.text-gray-500.dark:text-gray-400
       "Experience the world's greatest football tournament with us"]]

     [:div.max-w-6xl.mx-auto.px-4.py-10
      ;; Countdown
      [countdown-section]

      ;; Match Schedule
      [:div.mb-8
       [:h2.text-2xl.font-serif.font-bold.text-gray-900.dark:text-white.text-center.mb-2 "Match Schedule"]
       [:p.text-sm.text-gray-400.dark:text-gray-500.text-center.mb-8 "All times shown in Nairobi Time (EAT)"]]

      [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-5
       (for [fixture fixtures]
         ^{:key (:match fixture)}
         [match-card fixture])]]]))
