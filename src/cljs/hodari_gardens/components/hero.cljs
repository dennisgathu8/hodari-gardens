(ns hodari-gardens.components.hero
  "Hero section component with CTAs."
  (:require [hodari-gardens.routes :as routes]
            [re-frame.core :as rf]))

(defn- hero-countdown []
  (let [now    @(rf/subscribe [:current-time])
        target (js/Date. "2026-06-11T19:00:00Z")
        diff   (if now (- (.getTime target) (.getTime now)) 0)
        d      (Math/floor (/ diff 86400000))
        h      (Math/floor (/ (mod diff 86400000) 3600000))
        m      (Math/floor (/ (mod diff 3600000) 60000))
        s      (Math/floor (/ (mod diff 60000) 1000))]
    (when (and now (pos? diff))
      [:div.w-full.py-5.text-center.relative.z-20
       {:style {:background "linear-gradient(90deg, #1d4ed8 0%, #4338ca 50%, #dc2626 100%)"}}
       [:div.max-w-5xl.mx-auto.px-4.flex.flex-col.md:flex-row.items-center.justify-center.gap-6
        [:div.flex.items-center.gap-3
         [:span.text-3xl "⚽"]
         [:span.text-white.font-bold.text-xl.md:text-2xl "World Cup 2026"]]
        [:div.flex.gap-3
         (for [[v l] [[d "days"] [h "hrs"] [m "min"] [s "sec"]]]
           ^{:key l}
           [:div.flex.flex-col.items-center.bg-white.bg-opacity-20.rounded-lg.px-4.py-2
            [:span.text-2xl.md:text-3xl.font-bold.text-white.leading-none (str v)]
            [:span.text-xs.text-white.text-opacity-70.uppercase.font-semibold.mt-0.5 l]])]
        [:a.bg-white.text-blue-700.font-bold.text-sm.px-5.py-2.rounded-full.hover:bg-opacity-90.transition-all
         {:href (routes/href :world-cup)} "View Schedule →"]]])))

(defn hero-section
  "Main hero section with premium aesthetic and CTAs."
  []
  [:div
   [:section.relative.h-screen.flex.items-center.justify-center.text-white.overflow-hidden.bg-luxury-hero
    {:role "banner"}
    ;; Overlay
    [:div.absolute.inset-0.hero-overlay.opacity-60]
    [:div.absolute.inset-0.opacity-25
     {:style {:background-image "radial-gradient(circle at 20% 30%, rgba(34, 197, 94, 0.3) 0%, transparent 50%), 
                                   radial-gradient(circle at 80% 70%, rgba(20, 83, 45, 0.4) 0%, transparent 50%)"}}]
    
    ;; Content
    [:div.relative.z-10.max-w-5xl.mx-auto.px-4.text-center.animate-slide-up
     [:div.backdrop-blur-xl.p-8.md:p-16.rounded-3xl.border.border-gold-low.shadow-2xl
      {:style {:background "rgba(0,0,0,0.3)"}}
      [:h1.text-5xl.md:text-8xl.font-serif.font-bold.mb-6.tracking-tight.text-white.text-premium-glow
       "Hodari Gardens Resort"]
      [:p.text-xl.md:text-3xl.font-light.mb-8.text-garden-green-100.uppercase.text-premium-glow
       {:style {:letter-spacing "0.2em"}}
       "Where Luxury Meets Nature"]
      [:div.h-px.w-40.mx-auto.mb-8
       {:style {:background "linear-gradient(to right, transparent, rgba(234,179,8,0.5), transparent)"}}]
      [:p.text-lg.md:text-xl.mb-12.text-gray-200.italic.font-serif.text-premium-glow
       "Nakuru Lanet • A Sanctuary of Elegance"]
      
      ;; CTAs
      [:div.flex.flex-col.sm:flex-row.gap-8.justify-center.items-center
       [:a.bg-garden-green-600.hover:bg-garden-green-500.text-white.text-lg.font-semibold.px-12.py-4.rounded-full.transition-all.shadow-xl
        {:href (routes/href :accommodation)} "Exquisite Stays"]
       [:a.border.border-white.border-opacity-20.text-white.text-lg.px-12.py-4.rounded-full.transition-all
        {:style {:background "rgba(255,255,255,0.05)"}
         :href (routes/href :events)} "Grand Occasions"]]]]
    
    ;; Scroll indicator
    [:div.absolute.bottom-16.animate-bounce.opacity-40
     {:style {:left "50%" :transform "translateX(-50%)"}}
     [:svg.w-8.h-8 {:xmlns "http://www.w3.org/2000/svg" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor"}
      [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "1" :d "M19 14l-7 7m0 0l-7-7m7 7V3"}]]]
    
    ;; Countdown banner at bottom of hero
    [:div.absolute.bottom-0.left-0.w-full
     [hero-countdown]]]])
