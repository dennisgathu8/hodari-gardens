(ns hodari-gardens.components.hero
  "Hero section component with CTAs."
  (:require [hodari-gardens.routes :as routes]))

(defn hero-section
  "Main hero section with background image and CTAs."
  []
  [:section.relative.h-screen.flex.items-center.justify-center.text-white
   {:role "banner"}
   ;; Background image with overlay
   [:div.absolute.inset-0.bg-gradient-to-br.from-garden-green-900.to-garden-green-700.gradient-overlay
    ;; Placeholder for background image
    [:div.absolute.inset-0.bg-cover.bg-center
     {:style {:background-image "url('data:image/svg+xml,%3Csvg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 1200 800\"%3E%3Crect fill=\"%2316a34a\" width=\"1200\" height=\"800\"/%3E%3C/svg%3E')"
              :opacity "0.3"}}]]
   
   ;; Content
   [:div.relative.z-10.max-w-4xl.mx-auto.px-4.text-center
    [:h1.text-5xl.md:text-7xl.font-serif.font-bold.mb-6.animate-fade-in
     "Hodari Gardens Resort"]
    [:p.text-xl.md:text-2xl.mb-4.text-gray-100
     "Where Luxury Meets Nature"]
    [:p.text-lg.md:text-xl.mb-8.text-gray-200
     "Nakuru Lanet • Near Nakuru Specialist Hospital"]
    
    ;; CTAs
    [:div.flex.flex-col.sm:flex-row.gap-4.justify-center.items-center
     [:a.btn-primary.text-lg.px-8.py-4
      {:href (routes/href :accommodation)
       :aria-label "Book accommodation at Hodari Gardens"}
      "Book Accommodation"]
     [:a.btn-secondary.text-lg.px-8.py-4
      {:href (routes/href :events)
       :aria-label "Reserve event space at Hodari Gardens"}
      "Reserve Event Space"]
     [:a.btn-outline.text-lg.px-8.py-4
      {:href (routes/href :worldcup)
       :aria-label "View World Cup 2026 schedule"}
      "World Cup 2026 ⚽"]]
    
    ;; Scroll indicator
    [:div.mt-16.animate-bounce
     [:svg.w-6.h-6.mx-auto {:xmlns "http://www.w3.org/2000/svg" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor"}
      [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M19 14l-7 7m0 0l-7-7m7 7V3"}]]]]])
