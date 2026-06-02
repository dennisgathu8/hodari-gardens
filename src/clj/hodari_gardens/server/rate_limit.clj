(ns hodari-gardens.server.rate-limit
  "Token-bucket rate limiting Ring middleware for Hodari Gardens Resort."
  (:require [cheshire.core :as json]
            [clojure.string :as string]
            [malli.core :as m]))

;; Schema for Rate Limit config validation
(def RateLimitConfig
  [:map {:closed true}
   [:bucket-size [:int {:min 1}]]
   [:refill-rate-per-ms [:double {:min 0.0}]]])

(def ^:private config
  {:bucket-size 20
   ;; 10 tokens per minute (60,000 ms) = 1/6000 tokens per ms
   :refill-rate-per-ms (/ 10.0 60000.0)})

(when-not (m/validate RateLimitConfig config)
  (throw (ex-info "Rate limit configuration validation failed"
                  {:hodari/error-type :validation-error
                   :invalid-config config})))

;; PROD/DEV rate limiting state container
(defonce rate-limit-state (atom {}))

(defn get-client-ip
  "Extract the client IP from the request map.
   Supports X-Forwarded-For header with fallback to remote-addr."
  [req]
  (let [headers (:headers req)
        x-forwarded-for (or (get headers "x-forwarded-for")
                            (get headers "X-Forwarded-For"))]
    (if (and x-forwarded-for (not (string/blank? x-forwarded-for)))
      (-> (string/split x-forwarded-for #"\s*,\s*")
          first
          string/trim)
      (:remote-addr req))))

(defn- refill
  "Refill the token bucket based on time elapsed."
  [bucket now max-tokens refill-rate-per-ms]
  (let [elapsed (- now (:last-update bucket))
        added (* elapsed refill-rate-per-ms)
        tokens (min (double max-tokens) (+ (:tokens bucket) added))]
    {:tokens tokens :last-update now}))

(defn- consume-token
  "Pure function: given the current state map, an IP, timestamp, and config,
   returns [new-state result-map]. The swap function must be pure — all
   decision data is embedded in the returned state under [ip :last-result]."
  [state ip max-tokens refill-rate-per-ms now]
  (let [bucket     (or (get state ip) {:tokens (double max-tokens) :last-update now})
        refilled   (refill bucket now max-tokens refill-rate-per-ms)
        tokens     (:tokens refilled)]
    (if (>= tokens 1.0)
      (let [new-bucket (-> refilled
                           (update :tokens - 1.0)
                           (assoc :last-result {:allowed? true}))]
        (assoc state ip new-bucket))
      (let [wait-ms     (* (- 1.0 tokens) (/ 1.0 refill-rate-per-ms))
            retry-after (int (Math/ceil (/ wait-ms 1000.0)))
            new-bucket  (assoc refilled :last-result {:allowed? false
                                                      :retry-after (max 1 retry-after)})]
        (assoc state ip new-bucket)))))

(defn- check-limit!
  "Checks if the client IP has tokens available.
   Uses swap-vals! for an atomic, pure state transition — no side effects
   inside the swap function."
  [ip max-tokens refill-rate-per-ms now]
  (let [[_old-state new-state]
        (swap-vals! rate-limit-state
                    consume-token ip max-tokens refill-rate-per-ms now)]
    (get-in new-state [ip :last-result])))

(defn wrap-rate-limit
  "Ring middleware that rate limits requests using a token-bucket algorithm."
  [handler]
  (fn [req]
    (let [ip (get-client-ip req)
          now (System/currentTimeMillis)
          {:keys [allowed? retry-after]} (check-limit! ip (:bucket-size config) (:refill-rate-per-ms config) now)]
      (if allowed?
        (handler req)
        {:status 429
         :headers {"Content-Type" "application/json"
                   "Retry-After" (str retry-after)}
         :body (json/generate-string
                {:error "rate limit exceeded"
                 :retry_after_seconds retry-after})}))))

(comment
  ;; REPL exploration — evaluate these forms to verify behaviour
  (let [dummy-handler (fn [_] {:status 200 :body "OK"})
        limiter (wrap-rate-limit dummy-handler)
        req {:remote-addr "127.0.0.1" :headers {}}]
    (reset! rate-limit-state {})
    ;; Consume all tokens
    (dotimes [_ 20]
      (limiter req))
    ;; The 21st request should be blocked
    (limiter req))
  )
