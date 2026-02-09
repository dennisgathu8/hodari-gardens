(ns hodari-gardens.components.footer
  "Footer component with links and social media."
  (:require [hodari-gardens.routes :as routes]))

(defn footer
  "Main footer component."
  []
  [:footer.bg-gray-900.text-gray-300.py-12
   {:role "contentinfo"}
   [:div.max-w-7xl.mx-auto.px-4.sm:px-6.lg:px-8
    [:div.grid.grid-cols-1.md:grid-cols-4.gap-8.mb-8
     ;; About
     [:div
      [:h3.text-white.font-bold.text-lg.mb-4 "Hodari Gardens"]
      [:p.text-sm.mb-4
       "Experience luxury and nature in perfect harmony at Nakuru's premier resort."]
      [:p.text-sm
       "Nakuru Lanet, Near Nakuru Specialist Hospital"]]
     
     ;; Quick Links
     [:div
      [:h3.text-white.font-bold.text-lg.mb-4 "Quick Links"]
      [:ul.space-y-2.text-sm
       [:li [:a.hover:text-white.transition-colors {:href (routes/href :home)} "Home"]]
       [:li [:a.hover:text-white.transition-colors {:href (routes/href :accommodation)} "Accommodation"]]
       [:li [:a.hover:text-white.transition-colors {:href (routes/href :events)} "Events"]]
       [:li [:a.hover:text-white.transition-colors {:href (routes/href :worldcup)} "World Cup 2026"]]
       [:li [:a.hover:text-white.transition-colors {:href (routes/href :about)} "About Us"]]]]
     
     ;; Services
     [:div
      [:h3.text-white.font-bold.text-lg.mb-4 "Services"]
      [:ul.space-y-2.text-sm
       [:li "Luxury Accommodation"]
       [:li "Wedding Venues"]
       [:li "Corporate Events"]
       [:li "Bar & Lounge"]
       [:li "Sports Viewing"]
       [:li "Garden Events"]]]
     
      ;; Contact
      [:div
       [:h3.text-white.font-bold.text-lg.mb-4 "Contact"]
       [:ul.space-y-2.text-sm
        [:li [:a.hover:text-white.transition-colors {:href "tel:+254780693707"} "+254 780 693 707"]]
        [:li [:a.hover:text-white.transition-colors {:href "mailto:info@hodarigardens.co.ke"} "info@hodarigardens.co.ke"]]
        [:li.mt-4
         [:div.flex.space-x-4
          [:a.hover:text-white.transition-colors
           {:href "https://facebook.com/hodarigardens"
            :target "_blank"
            :rel "noopener noreferrer"
            :aria-label "Facebook"}
           [:svg.w-6.h-6 {:xmlns "http://www.w3.org/2000/svg" :fill "currentColor" :viewBox "0 0 24 24"}
            [:path {:d "M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"}]]]
          [:a.hover:text-white.transition-colors
           {:href "https://instagram.com/hodarigardens"
            :target "_blank"
            :rel "noopener noreferrer"
            :aria-label "Instagram"}
           [:svg.w-6.h-6 {:xmlns "http://www.w3.org/2000/svg" :fill "currentColor" :viewBox "0 0 24 24"}
            [:path {:d "M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zm0-2.163c-3.259 0-3.667.014-4.947.072-4.358.2-6.78 2.618-6.98 6.98-.059 1.281-.073 1.689-.073 4.948 0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98 1.281.058 1.689.072 4.948.072 3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98-1.281-.059-1.69-.073-4.949-.073zm0 5.838c-3.403 0-6.162 2.759-6.162 6.162s2.759 6.163 6.162 6.163 6.162-2.759 6.162-6.163c0-3.403-2.759-6.162-6.162-6.162zm0 10.162c-2.209 0-4-1.79-4-4 0-2.209 1.791-4 4-4s4 1.791 4 4c0 2.21-1.791 4-4 4zm6.406-11.845c-.796 0-1.441.645-1.441 1.44s.645 1.44 1.441 1.44c.795 0 1.439-.645 1.439-1.44s-.644-1.44-1.439-1.44z"}]]]
          [:a.hover:text-white.transition-colors
           {:href "https://x.com/hodarigardens"
            :target "_blank"
            :rel "noopener noreferrer"
            :aria-label "Twitter / X"}
           [:svg.w-6.h-6 {:xmlns "http://www.w3.org/2000/svg" :fill "currentColor" :viewBox "0 0 24 24"}
            [:path {:d "M18.901 1.153h3.68l-8.04 9.19L24 22.846h-7.406l-5.8-7.584-6.638 7.584H.474l8.6-9.83L0 1.154h7.594l5.243 6.932ZM17.61 20.644h2.039L6.486 3.24H4.298Z"}]]]
          [:a.hover:text-white.transition-colors
           {:href "https://tiktok.com/@hodarigardens"
            :target "_blank"
            :rel "noopener noreferrer"
            :aria-label "TikTok"}
           [:svg.w-6.h-6 {:xmlns "http://www.w3.org/2000/svg" :fill "currentColor" :viewBox "0 0 24 24"}
            [:path {:d "M12.525.02c1.31-.02 2.61-.01 3.91-.02.08 1.53.63 3.09 1.75 4.17 1.12 1.11 2.7 1.62 4.24 1.79v4.03c-1.44-.05-2.89-.35-4.2-.97-.57-.26-1.1-.59-1.62-.93-.01 2.92.01 5.84-.02 8.75-.03 1.4-.54 2.79-1.35 3.94-1.31 1.92-3.58 3.17-5.91 3.21-1.43.08-2.86-.31-4.08-1.03-2.02-1.19-3.44-3.37-3.59-5.71-.29-3.27 1.8-6.52 4.91-7.57.55-.2 1.12-.32 1.7-.4v4.05c-.16.05-.32.08-.48.15-1.1.41-1.93 1.41-2.04 2.58-.15 1.18.33 2.4 1.23 3.13.78.65 1.81.93 2.82.9 1.05-.03 2.05-.51 2.73-1.32.51-.59.81-1.36.81-2.14V.02z"}]]]]]]]
    
    ;; Bottom bar
    [:div.border-t.border-gray-800.pt-8.flex.flex-col.md:flex-row.justify-between.items-center.text-sm
     [:p "© 2026 Hodari Gardens Resort. All rights reserved."]
     [:p.mt-4.md:mt-0
      "Built with "
      [:span.text-red-500 "♥"]
      " using Clojure & ClojureScript"]]]]])
