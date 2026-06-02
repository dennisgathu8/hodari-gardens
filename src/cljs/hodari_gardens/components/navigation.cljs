(ns hodari-gardens.components.navigation
  "Navigation component with responsive mobile menu and dark mode toggle."
  (:require [re-frame.core :as rf]
            [hodari-gardens.routes :as routes]))

(defn nav-link
  "Navigation link component with active state."
  [route-name label]
  (let [current-route @(rf/subscribe [:current-route])
        active? (= current-route route-name)]
    [:a {:href (routes/href route-name)
         :class (str "nav-link px-3 py-2 rounded-md text-sm font-medium transition-colors "
                     (if active?
                       "bg-garden-green-100 dark:bg-garden-green-900 text-garden-green-700 dark:text-garden-green-300"
                       ""))
         :aria-current (when active? "page")}
     label]))

(defn mobile-menu
  "Mobile navigation menu (hamburger menu)."
  []
  (let [open? @(rf/subscribe [:mobile-menu-open?])]
    (when open?
      [:div.md:hidden.absolute.top-16.left-0.right-0.bg-white.dark:bg-gray-800.shadow-lg.z-50
       {:role "navigation"
        :aria-label "Mobile navigation"}
       [:div.px-4.py-4.space-y-2
        [:div [:a.block.py-2.nav-link {:href (routes/href :world-cup)
                                        :on-click #(rf/dispatch [:close-mobile-menu])}
               "World Cup 2026"]]
        [:div [:a.block.py-2.nav-link {:href (routes/href :accommodation)
                                        :on-click #(rf/dispatch [:close-mobile-menu])}
               "Accommodation"]]
        [:div [:a.block.py-2.nav-link {:href (routes/href :drinks)
                                        :on-click #(rf/dispatch [:close-mobile-menu])}
               "Drinks & Dining"]]
        [:div [:a.block.py-2.nav-link {:href (routes/href :events)
                                        :on-click #(rf/dispatch [:close-mobile-menu])}
               "Events"]]
        [:div [:a.block.py-2.nav-link {:href (routes/href :about)
                                        :on-click #(rf/dispatch [:close-mobile-menu])}
               "About"]]
        [:div [:a.block.py-2.nav-link {:href (routes/href :contact)
                                        :on-click #(rf/dispatch [:close-mobile-menu])}
               "Location"]]]])))

(defn navigation
  "Main navigation bar component."
  []
  (let [dark-mode? @(rf/subscribe [:dark-mode?])]
    [:nav.bg-white.dark:bg-gray-800.shadow-md.sticky.top-0.z-40
     {:role "navigation"
      :aria-label "Main navigation"}
     [:div.max-w-7xl.mx-auto.px-4.sm:px-6.lg:px-8
      [:div.flex.justify-between.items-center.h-16
       ;; Logo
       [:div.flex-shrink-0
        [:a.flex.items-center {:href (routes/href :home)
                               :aria-label "Hodari Gardens Home"}
         [:span.text-2xl.font-serif.font-bold.text-garden-green-600.dark:text-garden-green-400
          "Hodari Gardens"]]]
       
       ;; Desktop Navigation
       [:div.hidden.md:flex.md:items-center.md:space-x-4
        [nav-link :world-cup "World Cup"]
        [nav-link :contact "Location"]
        [:a.ml-4.flex.items-center.text-garden-green-600.dark:text-garden-green-400.font-semibold.hover:text-garden-green-700.transition-colors
         {:href "https://wa.me/254780693707"
          :target "_blank"
          :rel "noopener noreferrer"}
         [:svg.w-5.h-5.mr-1 {:viewBox "0 0 24 24" :fill "currentColor"}
          [:path {:d "M17.472 14.382c-.297-.149-1.758-.867-2.03-.967-.273-.099-.471-.148-.67.15-.197.297-.767.966-.94 1.164-.173.199-.347.223-.644.075-.297-.15-1.255-.463-2.39-1.475-.883-.788-1.48-1.761-1.653-2.059-.173-.297-.018-.458.13-.606.134-.133.298-.347.446-.52.149-.174.198-.298.298-.497.099-.198.05-.371-.025-.52-.075-.149-.669-1.612-.916-2.207-.242-.579-.487-.5-.669-.51-.173-.008-.371-.01-.57-.01-.198 0-.52.074-.792.372-.272.297-1.04 1.016-1.04 2.479 0 1.462 1.065 2.875 1.213 3.074.149.198 2.096 3.2 5.077 4.487.709.306 1.262.489 1.694.625.712.227 1.36.195 1.871.118.571-.085 1.758-.719 2.006-1.413.248-.694.248-1.289.173-1.413-.074-.124-.272-.198-.57-.347m-5.421 7.403h-.004a9.87 9.87 0 01-5.031-1.378l-.361-.214-3.741.982.998-3.648-.235-.374a9.86 9.86 0 01-1.51-5.26c.001-5.45 4.436-9.884 9.888-9.884 2.64 0 5.122 1.03 6.988 2.898a9.825 9.825 0 012.893 6.994c-.003 5.45-4.437 9.884-9.885 9.884m8.413-18.297A11.815 11.815 0 0012.05 0C5.495 0 .16 5.335.157 11.892c0 2.096.547 4.142 1.588 5.945L0 24l6.335-1.662c1.72.94 3.659 1.437 5.71 1.438h.005c6.554 0 11.89-5.335 11.893-11.893a11.821 11.821 0 00-3.48-8.413z"}]]
         "Inquire"]]
       
       ;; Dark Mode Toggle & Mobile Menu Button
       [:div.flex.items-center.space-x-2
        ;; Dark mode toggle
        [:button.p-2.rounded-lg.hover:bg-gray-100.dark:hover:bg-gray-700.transition-colors
         {:on-click #(rf/dispatch [:toggle-dark-mode])
          :aria-label (if dark-mode? "Switch to light mode" "Switch to dark mode")
          :title (if dark-mode? "Switch to light mode" "Switch to dark mode")}
         (if dark-mode?
           [:svg.w-5.h-5.text-yellow-400 {:xmlns "http://www.w3.org/2000/svg" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor"}
            [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z"}]]
           [:svg.w-5.h-5.text-gray-700.dark:text-gray-300 {:xmlns "http://www.w3.org/2000/svg" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor"}
            [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z"}]])]
        
        ;; Mobile menu button
        [:button.md:hidden.p-2.rounded-lg.hover:bg-gray-100.dark:hover:bg-gray-700.transition-colors
         {:on-click #(rf/dispatch [:toggle-mobile-menu])
          :aria-label "Toggle mobile menu"
          :aria-expanded @(rf/subscribe [:mobile-menu-open?])}
         [:svg.w-6.h-6 {:xmlns "http://www.w3.org/2000/svg" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor"}
          [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M4 6h16M4 12h16M4 18h16"}]]]]]]
     
     ;; Mobile menu
     [mobile-menu]]))
