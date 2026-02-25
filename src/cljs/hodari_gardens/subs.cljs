(ns hodari-gardens.subs
  "Re-frame subscriptions for Hodari Gardens application.
   Provides reactive queries to application state."
  (:require [re-frame.core :as rf]))

;; Basic subscriptions
(rf/reg-sub
 :current-route
 (fn [db _]
   (:current-route db)))

(rf/reg-sub
 :dark-mode?
 (fn [db _]
   (:dark-mode? db)))

(rf/reg-sub
 :mobile-menu-open?
 (fn [db _]
   (:mobile-menu-open? db)))

(rf/reg-sub
 :loading?
 (fn [db _]
   (:loading? db)))

(rf/reg-sub
 :error
 (fn [db _]
   (:error db)))

;; Data subscriptions
(rf/reg-sub
 :rooms
 (fn [db _]
   (:rooms db)))

(rf/reg-sub
 :events
 (fn [db _]
   (:events db)))

(rf/reg-sub
 :event-packages
 (fn [db _]
   (get-in db [:events :event-packages])))

(rf/reg-sub
 :gallery
 (fn [db _]
   (get-in db [:events :gallery])))

(rf/reg-sub
 :drinks
 (fn [db _]
   (:drinks db)))

(rf/reg-sub
 :drinks-menu
 (fn [db _]
   (get-in db [:drinks :menu])))

(rf/reg-sub
 :happy-hours
 (fn [db _]
   (get-in db [:drinks :happy-hours])))

(rf/reg-sub
 :worldcup
 (fn [db _]
   (:worldcup db)))

(rf/reg-sub
 :current-time
 (fn [db _]
   (or (:current-time db) (js/Date.))))

(rf/reg-sub
 :worldcup-countdown
 :<- [:current-time]
 :<- [:worldcup-tournament]
 (fn [[current-time tournament] _]
   (if-let [start-date (:start-date tournament)]
     (let [start-ms (.getTime (js/Date. start-date))
           now-ms (.getTime current-time)
           diff-ms (- start-ms now-ms)]
       (if (pos? diff-ms)
         (let [secs (Math/floor (/ diff-ms 1000))
               mins (Math/floor (/ secs 60))
               hrs (Math/floor (/ mins 60))
               days (Math/floor (/ hrs 24))]
           {:days days
            :hours (mod hrs 24)
            :minutes (mod mins 60)
            :seconds (mod secs 60)})
         {:days 0 :hours 0 :minutes 0 :seconds 0}))
     {:days 0 :hours 0 :minutes 0 :seconds 0})))

(rf/reg-sub
 :worldcup-tournament
 (fn [db _]
   (get-in db [:worldcup :tournament])))

(rf/reg-sub
 :worldcup-matches
 (fn [db _]
   (get-in db [:worldcup :matches])))

(rf/reg-sub
 :viewing-packages
 (fn [db _]
   (get-in db [:worldcup :viewing-packages])))

(rf/reg-sub
 :contact
 (fn [db _]
   (:contact db)))

;; Form subscriptions
(rf/reg-sub
 :booking-form
 (fn [db _]
   (:booking-form db)))

(rf/reg-sub
 :inquiry-form
 (fn [db _]
   (:inquiry-form db)))

(rf/reg-sub
 :form-submitting?
 (fn [db _]
   (:form-submitting? db)))

;; Lightbox subscriptions
(rf/reg-sub
 :lightbox
 (fn [db _]
   (:lightbox db)))

(rf/reg-sub
 :lightbox-open?
 (fn [db _]
   (get-in db [:lightbox :open?])))

(rf/reg-sub
 :lightbox-current-image
 (fn [db _]
   (let [lightbox (:lightbox db)
         index (:current-index lightbox)
         images (:images lightbox)]
     (when (and images (< index (count images)))
       (nth images index)))))
