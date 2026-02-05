(ns hodari-gardens.components.contact
  "Contact section with forms and map integration."
  (:require [re-frame.core :as rf]))

(defn contact-form
  "Contact/inquiry form component."
  []
  (let [form @(rf/subscribe [:inquiry-form])
        submitting? @(rf/subscribe [:form-submitting?])]
    [:form.space-y-4
     {:on-submit (fn [e]
                   (.preventDefault e)
                   (rf/dispatch [:submit-inquiry]))}
     [:div
      [:label.block.text-sm.font-medium.text-gray-700.dark:text-gray-300.mb-1
       {:for "name"}
       "Full Name *"]
      [:input.input-field
       {:id "name"
        :type "text"
        :required true
        :value (:name form)
        :on-change #(rf/dispatch [:update-inquiry-form :name (-> % .-target .-value)])
        :aria-required "true"}]]
     [:div
      [:label.block.text-sm.font-medium.text-gray-700.dark:text-gray-300.mb-1
       {:for "email"}
       "Email Address *"]
      [:input.input-field
       {:id "email"
        :type "email"
        :required true
        :value (:email form)
        :on-change #(rf/dispatch [:update-inquiry-form :email (-> % .-target .-value)])
        :aria-required "true"}]]
     [:div
      [:label.block.text-sm.font-medium.text-gray-700.dark:text-gray-300.mb-1
       {:for "phone"}
       "Phone Number *"]
      [:input.input-field
       {:id "phone"
        :type "tel"
        :required true
        :value (:phone form)
        :on-change #(rf/dispatch [:update-inquiry-form :phone (-> % .-target .-value)])
        :aria-required "true"}]]
     [:div
      [:label.block.text-sm.font-medium.text-gray-700.dark:text-gray-300.mb-1
       {:for "subject"}
       "Subject *"]
      [:input.input-field
       {:id "subject"
        :type "text"
        :required true
        :value (:subject form)
        :on-change #(rf/dispatch [:update-inquiry-form :subject (-> % .-target .-value)])
        :aria-required "true"}]]
     [:div
      [:label.block.text-sm.font-medium.text-gray-700.dark:text-gray-300.mb-1
       {:for "message"}
       "Message *"]
      [:textarea.input-field
       {:id "message"
        :rows 5
        :required true
        :value (:message form)
        :on-change #(rf/dispatch [:update-inquiry-form :message (-> % .-target .-value)])
        :aria-required "true"}]]
     [:button.btn-primary.w-full
      {:type "submit"
       :disabled submitting?}
      (if submitting? "Sending..." "Send Message")]]))

(defn contact-info-card
  "Contact information display card."
  [icon title content]
  [:div.flex.items-start.space-x-4
   [:div.flex-shrink-0.w-12.h-12.bg-garden-green-100.dark:bg-garden-green-900.rounded-lg.flex.items-center.justify-center
    [:svg.w-6.h-6.text-garden-green-600.dark:text-garden-green-400 {:xmlns "http://www.w3.org/2000/svg" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor"}
     icon]]
   [:div
    [:h3.font-semibold.text-gray-900.dark:text-white.mb-1 title]
    [:div.text-gray-600.dark:text-gray-300.text-sm content]]])

(defn contact-section
  "Full contact section."
  []
  (rf/dispatch [:fetch-contact])
  (fn []
    (let [contact-data @(rf/subscribe [:contact])
          contact-info (:contact contact-data)
          nearby (:nearby-attractions contact-data)]
      [:section.py-16.bg-white.dark:bg-gray-900
       [:div.max-w-7xl.mx-auto.px-4.sm:px-6.lg:px-8
        [:div.text-center.mb-12
         [:h1.section-title "Contact Us"]
         [:p.section-subtitle
          "Get in touch with us for bookings and inquiries"]]
        
        [:div.grid.grid-cols-1.lg:grid-cols-2.gap-12
         ;; Contact Information
         [:div
          [:h2.text-2xl.font-bold.mb-6.text-gray-900.dark:text-white
           "Get In Touch"]
          [:div.space-y-6.mb-8
           [contact-info-card
            [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"}]
            "Location"
            [:div
             [:p (get-in contact-info [:address :street])]
             [:p (get-in contact-info [:address :area])]
             [:p (get-in contact-info [:address :landmark])]
             [:p (str (get-in contact-info [:address :city]) ", " (get-in contact-info [:address :country]))]]]
           
           [contact-info-card
            [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"}]
            "Phone"
            [:div
             [:p [:a.hover:text-garden-green-600 {:href (str "tel:" (get-in contact-info [:phone :primary]))}
                  (get-in contact-info [:phone :primary])]]
             [:p [:a.hover:text-garden-green-600 {:href (str "tel:" (get-in contact-info [:phone :secondary]))}
                  (get-in contact-info [:phone :secondary])]]
             [:p.mt-2
              [:a.inline-flex.items-center.px-4.py-2.bg-green-500.text-white.rounded-lg.hover:bg-green-600
               {:href (str "https://wa.me/" (clojure.string/replace (get-in contact-info [:phone :whatsapp]) #"[^0-9]" ""))
                :target "_blank"
                :rel "noopener noreferrer"}
               "WhatsApp Us"]]]]
           
           [contact-info-card
            [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"}]
            "Email"
            [:div
             [:p [:a.hover:text-garden-green-600 {:href (str "mailto:" (get-in contact-info [:email :general]))}
                  (get-in contact-info [:email :general])]]
             [:p [:a.hover:text-garden-green-600 {:href (str "mailto:" (get-in contact-info [:email :bookings]))}
                  (get-in contact-info [:email :bookings])]]
             [:p [:a.hover:text-garden-green-600 {:href (str "mailto:" (get-in contact-info [:email :events]))}
                  (get-in contact-info [:email :events])]]]]]
          
          ;; Map placeholder
          [:div.mt-8
           [:h3.text-xl.font-bold.mb-4.text-gray-900.dark:text-white "Find Us"]
           [:div.aspect-w-16.aspect-h-9.bg-gray-200.dark:bg-gray-700.rounded-lg.overflow-hidden
            [:div.flex.items-center.justify-center.text-gray-500
             "Map: " (get-in contact-info [:coordinates :lat]) ", " (get-in contact-info [:coordinates :lng])]]]]
         
         ;; Contact Form
         [:div
          [:h2.text-2xl.font-bold.mb-6.text-gray-900.dark:text-white
           "Send Us a Message"]
          [contact-form]]]
        
        ;; Nearby Attractions
        (when nearby
          [:div.mt-16
           [:h2.text-2xl.font-bold.mb-6.text-center.text-gray-900.dark:text-white
            "Nearby Attractions"]
           [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-6
            (for [attraction nearby]
              ^{:key (:name attraction)}
              [:div.card.p-6
               [:h3.font-bold.text-lg.mb-2.text-gray-900.dark:text-white
                (:name attraction)]
               [:p.text-sm.text-gray-600.dark:text-gray-400.mb-2
                (:distance attraction) " away"]
               [:p.text-sm.text-gray-600.dark:text-gray-300
                (:description attraction)]])]])]])))
