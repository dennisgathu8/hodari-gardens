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
        [:div [:a.block.py-2.nav-link {:href (routes/href :home)
                                        :on-click #(rf/dispatch [:close-mobile-menu])}
               "Home"]]
        [:div [:a.block.py-2.nav-link {:href (routes/href :accommodation)
                                        :on-click #(rf/dispatch [:close-mobile-menu])}
               "Accommodation"]]
        [:div [:a.block.py-2.nav-link {:href (routes/href :drinks)
                                        :on-click #(rf/dispatch [:close-mobile-menu])}
               "Drinks & Dining"]]
        [:div [:a.block.py-2.nav-link {:href (routes/href :events)
                                        :on-click #(rf/dispatch [:close-mobile-menu])}
               "Events"]]
        [:div [:a.block.py-2.nav-link {:href (routes/href :worldcup)
                                        :on-click #(rf/dispatch [:close-mobile-menu])}
               "World Cup 2026"]]
        [:div [:a.block.py-2.nav-link {:href (routes/href :about)
                                        :on-click #(rf/dispatch [:close-mobile-menu])}
               "About"]]
        [:div [:a.block.py-2.nav-link {:href (routes/href :contact)
                                        :on-click #(rf/dispatch [:close-mobile-menu])}
               "Contact"]]]])))

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
        [nav-link :home "Home"]
        [nav-link :accommodation "Accommodation"]
        [nav-link :drinks "Drinks & Dining"]
        [nav-link :events "Events"]
        [nav-link :worldcup "World Cup 2026"]
        [nav-link :about "About"]
        [nav-link :contact "Contact"]]
       
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
