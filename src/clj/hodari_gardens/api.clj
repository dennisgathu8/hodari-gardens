(ns hodari-gardens.api
  "API handlers for Hodari Gardens Resort.
   Provides endpoints for rooms, events, drinks, World Cup data, and bookings."
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [ring.util.response :as response]))

;; Data loading utilities
(defn load-edn-resource
  "Load and parse EDN file from resources directory."
  [filename]
  (-> (str "data/" filename)
      io/resource
      slurp
      edn/read-string))

;; Cached data (loaded once at startup)
(def rooms-data (delay (load-edn-resource "rooms.edn")))
(def events-data (delay (load-edn-resource "events.edn")))
(def drinks-data (delay (load-edn-resource "drinks.edn")))
(def worldcup-data (delay (load-edn-resource "worldcup.edn")))
(def contact-data (delay (load-edn-resource "contact.edn")))

;; Room endpoints
(defn get-rooms
  "Return all available rooms."
  [_request]
  (response/response {:rooms (:rooms @rooms-data)}))

(defn get-room-by-id
  "Return specific room by ID."
  [request]
  (let [room-id (get-in request [:path-params :id])
        room (->> (:rooms @rooms-data)
                  (filter #(= (:id %) room-id))
                  first)]
    (if room
      (response/response {:room room})
      (-> (response/response {:error "Room not found"})
          (response/status 404)))))

;; Event endpoints
(defn get-events
  "Return all event packages and gallery."
  [_request]
  (response/response @events-data))

(defn get-event-by-id
  "Return specific event package by ID."
  [request]
  (let [event-id (get-in request [:path-params :id])
        event (->> (:event-packages @events-data)
                   (filter #(= (:id %) event-id))
                   first)]
    (if event
      (response/response {:event event})
      (-> (response/response {:error "Event package not found"})
          (response/status 404)))))

;; Drinks endpoints
(defn get-drinks
  "Return complete drinks menu and happy hours."
  [_request]
  (response/response @drinks-data))

;; World Cup endpoints
(defn get-worldcup-data
  "Return complete World Cup data including tournament info and packages."
  [_request]
  (response/response @worldcup-data))

(defn get-matches
  "Return World Cup matches, optionally filtered by stage or date."
  [request]
  (let [stage (get-in request [:query-params "stage"])
        date (get-in request [:query-params "date"])
        matches (:matches @worldcup-data)
        filtered-matches (cond->> matches
                           stage (filter #(= (name (:stage %)) stage))
                           date (filter #(= (:date %) date)))]
    (response/response {:matches filtered-matches})))

;; Contact endpoint
(defn get-contact-info
  "Return contact information and location details."
  [_request]
  (response/response @contact-data))

;; Booking endpoints (mock implementations - log to console)
(defn submit-booking
  "Handle accommodation booking submission.
   In production, this would integrate with a booking system."
  [request]
  (let [booking-data (:body request)]
    (println "=== NEW BOOKING RECEIVED ===")
    (println "Booking data:" booking-data)
    (println "===========================")
    (response/response {:success true
                        :message "Booking received! We'll contact you shortly."
                        :booking-id (str "BK-" (System/currentTimeMillis))})))

(defn submit-inquiry
  "Handle general inquiry or event booking submission.
   In production, this would send emails or integrate with CRM."
  [request]
  (let [inquiry-data (:body request)]
    (println "=== NEW INQUIRY RECEIVED ===")
    (println "Inquiry data:" inquiry-data)
    (println "============================")
    (response/response {:success true
                        :message "Thank you for your inquiry! We'll respond within 24 hours."
                        :inquiry-id (str "INQ-" (System/currentTimeMillis))})))
