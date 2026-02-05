(ns hodari-gardens.events
  "Re-frame event handlers for Hodari Gardens application.
   Handles all state mutations and side effects."
  (:require [re-frame.core :as rf]
            [hodari-gardens.db :as db]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]))

;; Initialization
(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   db/default-db))

;; Navigation
(rf/reg-event-db
 :set-current-route
 (fn [db [_ route]]
   (assoc db :current-route route)))

;; UI State
(rf/reg-event-db
 :toggle-dark-mode
 (fn [db _]
   (let [dark? (not (:dark-mode? db))]
     ;; Update DOM class for Tailwind dark mode
     (if dark?
       (.add (.-classList js/document.documentElement) "dark")
       (.remove (.-classList js/document.documentElement) "dark"))
     (assoc db :dark-mode? dark?))))

(rf/reg-event-db
 :toggle-mobile-menu
 (fn [db _]
   (update db :mobile-menu-open? not)))

(rf/reg-event-db
 :close-mobile-menu
 (fn [db _]
   (assoc db :mobile-menu-open? false)))

;; Data Loading
(rf/reg-event-fx
 :fetch-rooms
 (fn [{:keys [db]} _]
   {:db (assoc db :loading? true)
    :http-xhrio {:method :get
                 :uri "/api/rooms"
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:fetch-rooms-success]
                 :on-failure [:fetch-failure]}}))

(rf/reg-event-db
 :fetch-rooms-success
 (fn [db [_ response]]
   (-> db
       (assoc :rooms (:rooms response))
       (assoc :loading? false))))

(rf/reg-event-fx
 :fetch-events
 (fn [{:keys [db]} _]
   {:db (assoc db :loading? true)
    :http-xhrio {:method :get
                 :uri "/api/events"
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:fetch-events-success]
                 :on-failure [:fetch-failure]}}))

(rf/reg-event-db
 :fetch-events-success
 (fn [db [_ response]]
   (-> db
       (assoc :events response)
       (assoc :loading? false))))

(rf/reg-event-fx
 :fetch-drinks
 (fn [{:keys [db]} _]
   {:db (assoc db :loading? true)
    :http-xhrio {:method :get
                 :uri "/api/drinks"
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:fetch-drinks-success]
                 :on-failure [:fetch-failure]}}))

(rf/reg-event-db
 :fetch-drinks-success
 (fn [db [_ response]]
   (-> db
       (assoc :drinks response)
       (assoc :loading? false))))

(rf/reg-event-fx
 :fetch-worldcup
 (fn [{:keys [db]} _]
   {:db (assoc db :loading? true)
    :http-xhrio {:method :get
                 :uri "/api/worldcup"
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:fetch-worldcup-success]
                 :on-failure [:fetch-failure]}}))

(rf/reg-event-db
 :fetch-worldcup-success
 (fn [db [_ response]]
   (-> db
       (assoc :worldcup response)
       (assoc :loading? false))))

(rf/reg-event-fx
 :fetch-contact
 (fn [{:keys [db]} _]
   {:db (assoc db :loading? true)
    :http-xhrio {:method :get
                 :uri "/api/contact"
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:fetch-contact-success]
                 :on-failure [:fetch-failure]}}))

(rf/reg-event-db
 :fetch-contact-success
 (fn [db [_ response]]
   (-> db
       (assoc :contact response)
       (assoc :loading? false))))

(rf/reg-event-db
 :fetch-failure
 (fn [db [_ error]]
   (-> db
       (assoc :loading? false)
       (assoc :error (str "Failed to load data: " error)))))

;; Form Handling
(rf/reg-event-db
 :update-booking-form
 (fn [db [_ field value]]
   (assoc-in db [:booking-form field] value)))

(rf/reg-event-db
 :update-inquiry-form
 (fn [db [_ field value]]
   (assoc-in db [:inquiry-form field] value)))

(rf/reg-event-fx
 :submit-booking
 (fn [{:keys [db]} _]
   {:db (assoc db :form-submitting? true)
    :http-xhrio {:method :post
                 :uri "/api/booking"
                 :params (:booking-form db)
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:submit-booking-success]
                 :on-failure [:submit-booking-failure]}}))

(rf/reg-event-db
 :submit-booking-success
 (fn [db [_ response]]
   (js/alert (:message response))
   (-> db
       (assoc :form-submitting? false)
       (assoc :booking-form (:booking-form db/default-db)))))

(rf/reg-event-db
 :submit-booking-failure
 (fn [db [_ error]]
   (js/alert "Failed to submit booking. Please try again.")
   (assoc db :form-submitting? false)))

(rf/reg-event-fx
 :submit-inquiry
 (fn [{:keys [db]} _]
   {:db (assoc db :form-submitting? true)
    :http-xhrio {:method :post
                 :uri "/api/inquiry"
                 :params (:inquiry-form db)
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:submit-inquiry-success]
                 :on-failure [:submit-inquiry-failure]}}))

(rf/reg-event-db
 :submit-inquiry-success
 (fn [db [_ response]]
   (js/alert (:message response))
   (-> db
       (assoc :form-submitting? false)
       (assoc :inquiry-form (:inquiry-form db/default-db)))))

(rf/reg-event-db
 :submit-inquiry-failure
 (fn [db [_ error]]
   (js/alert "Failed to submit inquiry. Please try again.")
   (assoc db :form-submitting? false)))

;; Lightbox
(rf/reg-event-db
 :open-lightbox
 (fn [db [_ images current-index]]
   (assoc db :lightbox {:open? true
                        :images images
                        :current-index current-index})))

(rf/reg-event-db
 :close-lightbox
 (fn [db _]
   (assoc db :lightbox {:open? false
                        :images []
                        :current-index 0})))

(rf/reg-event-db
 :lightbox-next
 (fn [db _]
   (let [current (:current-index (:lightbox db))
         total (count (:images (:lightbox db)))
         next-index (mod (inc current) total)]
     (assoc-in db [:lightbox :current-index] next-index))))

(rf/reg-event-db
 :lightbox-prev
 (fn [db _]
   (let [current (:current-index (:lightbox db))
         total (count (:images (:lightbox db)))
         prev-index (mod (dec current) total)]
     (assoc-in db [:lightbox :current-index] prev-index))))
