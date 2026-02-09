(ns hodari-gardens.components.contact
  "Contact section with forms and map integration."
  (:require [re-frame.core :as rf]
            [clojure.string :as str]))

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
              (when-let [wa-num (get-in contact-info [:phone :whatsapp])]
                [:a.inline-flex.items-center.px-4.py-2.bg-green-500.text-white.rounded-lg.hover:bg-green-600
                 {:href (str "https://wa.me/" (str/replace wa-num #"[^0-9]" ""))
                  :target "_blank"
                  :rel "noopener noreferrer"}
                 [:svg.w-5.h-5.mr-2 {:xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 24 24" :fill "currentColor"}
                  [:path {:d "M17.472 14.382c-.297-.149-1.758-.867-2.03-.967-.273-.099-.471-.148-.67.15-.197.297-.767.966-.94 1.164-.173.199-.347.223-.644.075-.297-.15-1.255-.463-2.39-1.475-.883-.788-1.48-1.761-1.653-2.059-.173-.297-.018-.458.13-.606.134-.133.298-.347.446-.52.149-.174.198-.298.298-.497.099-.198.05-.371-.025-.52-.075-.149-.669-1.612-.916-2.207-.242-.579-.487-.5-.669-.51-.173-.008-.371-.01-.57-.01-.198 0-.52.074-.792.372-.272.297-1.04 1.016-1.04 2.479 0 1.462 1.065 2.875 1.213 3.074.149.198 2.096 3.2 5.077 4.487.709.306 1.262.489 1.694.625.712.227 1.36.195 1.871.118.571-.085 1.758-.719 2.006-1.413.248-.694.248-1.289.173-1.413-.074-.124-.272-.198-.57-.347m-5.421 7.403h-.004a9.87 9.87 0 01-5.031-1.378l-.361-.214-3.741.982.998-3.648-.235-.374a9.86 9.86 0 01-1.51-5.26c.001-5.45 4.436-9.884 9.888-9.884 2.64 0 5.122 1.03 6.988 2.898a9.825 9.825 0 012.893 6.994c-.003 5.45-4.437 9.884-9.885 9.884m8.413-18.297A11.815 11.815 0 0012.05 0C5.495 0 .16 5.335.157 11.892c0 2.096.547 4.142 1.588 5.945L.057 24l6.305-1.654a11.882 11.882 0 005.683 1.448h.005c6.554 0 11.89-5.335 11.893-11.893a11.821 11.821 0 00-3.48-8.413Z"}]]
                 "WhatsApp Us"])])]]]
           
           [contact-info-card
            [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"}]
            "Email"
            [:div
             [:p [:a.hover:text-garden-green-600 {:href (str "tel:" (get-in contact-info [:phone :primary]))}
                  (get-in contact-info [:phone :primary])]]
             [:p [:a.hover:text-garden-green-600 {:href (str "tel:" (get-in contact-info [:phone :secondary]))}
                  (get-in contact-info [:phone :secondary])]]
             [:p [:a.hover:text-garden-green-600 {:href (str "mailto:" (get-in contact-info [:email :general]))}
                  (get-in contact-info [:email :general])]]]]]
          
          ;; Map integration
          [:div.mt-8
           [:h3.text-xl.font-bold.mb-4.text-gray-900.dark:text-white "Find Us"]
           [:div.aspect-w-16.aspect-h-9.rounded-lg.overflow-hidden.border-0
            [:iframe
             {:src "https://www.google.com/maps/embed?pb=!1m18!1m12!1m13!1d15958.859664426863!2d36.1408333!3d-0.2980556!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x1829910000000001%3A0x0!2sHodari%20Gardens%20Resort!5e0!3m2!1sen!2ske!4v1700000000000"
              :width "100%"
              :height "100%"
              :style {:border 0}
              :allow-full-screen ""
              :loading "lazy"}]]]]
         
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
