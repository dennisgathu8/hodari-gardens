(ns hodari-gardens.server.middleware-test
  "Isolation tests for Ring middleware in Hodari Gardens Resort.

   Each middleware function is tested in isolation, wrapping a trivial
   handler — no composed stack, no HTTP server, no mocking framework.

   Coverage (≥ 12 assertions):
     • wrap-security-headers — all 6 header keys present, specific values
     • wrap-cache-headers — immutable for /js/ and /css/, no-cache otherwise
     • wrap-security-headers merge behaviour — existing headers preserved"
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.string]
            [hodari-gardens.server.middleware
             :refer [wrap-security-headers wrap-cache-headers]]))

;; ---------------------------------------------------------------------------
;; Helpers
;; ---------------------------------------------------------------------------

(def ^:private ok-handler
  "Trivial Ring handler that returns a 200 with empty headers."
  (constantly {:status 200 :headers {} :body "ok"}))

(defn- make-req
  "Build a minimal Ring request map for the given URI."
  [uri]
  {:uri uri :request-method :get :headers {}})

;; ---------------------------------------------------------------------------
;; 1. wrap-security-headers — presence and values
;; ---------------------------------------------------------------------------

(deftest security-headers-presence-test
  (testing "wrap-security-headers injects all 6 headers into a bare response"
    (let [handler (wrap-security-headers ok-handler)
          resp    (handler (make-req "/"))]

      ;; Assertion 1
      (is (contains? (:headers resp) "Strict-Transport-Security")
          "HSTS header must be present")

      ;; Assertion 2
      (is (contains? (:headers resp) "X-Frame-Options")
          "X-Frame-Options header must be present")

      ;; Assertion 3
      (is (contains? (:headers resp) "X-Content-Type-Options")
          "X-Content-Type-Options header must be present")

      ;; Assertion 4
      (is (contains? (:headers resp) "Referrer-Policy")
          "Referrer-Policy header must be present")

      ;; Assertion 5
      (is (contains? (:headers resp) "Permissions-Policy")
          "Permissions-Policy header must be present")

      ;; Assertion 6
      (is (contains? (:headers resp) "Content-Security-Policy")
          "CSP header must be present"))))

(deftest security-headers-values-test
  (testing "wrap-security-headers sets correct values"
    (let [handler (wrap-security-headers ok-handler)
          resp    (handler (make-req "/"))
          hdrs    (:headers resp)]

      ;; Assertion 7
      (is (= "DENY" (get hdrs "X-Frame-Options"))
          "X-Frame-Options must be DENY")

      ;; Assertion 8
      (is (= "nosniff" (get hdrs "X-Content-Type-Options"))
          "X-Content-Type-Options must be nosniff")

      ;; Assertion 9
      (is (clojure.string/includes?
           (get hdrs "Strict-Transport-Security")
           "max-age=31536000")
          "HSTS must contain max-age=31536000")

      ;; Assertion 10
      (is (clojure.string/includes?
           (get hdrs "Content-Security-Policy")
           "default-src")
          "CSP must contain default-src directive"))))

;; ---------------------------------------------------------------------------
;; 2. wrap-cache-headers — static vs dynamic paths
;; ---------------------------------------------------------------------------

(deftest cache-headers-immutable-test
  (testing "wrap-cache-headers sets immutable for /js/ and /css/ paths"
    (let [handler (wrap-cache-headers ok-handler)]

      ;; Assertion 11
      (is (= "public, max-age=31536000, immutable"
             (get-in (handler (make-req "/js/app.js"))
                     [:headers "Cache-Control"]))
          "/js/app.js must get immutable cache header")

      ;; Assertion 12
      (is (= "public, max-age=31536000, immutable"
             (get-in (handler (make-req "/css/styles.css"))
                     [:headers "Cache-Control"]))
          "/css/styles.css must get immutable cache header"))))

(deftest cache-headers-no-cache-test
  (testing "wrap-cache-headers sets no-cache for non-static paths"
    (let [handler (wrap-cache-headers ok-handler)]

      ;; Assertion 13
      (is (= "no-cache"
             (get-in (handler (make-req "/"))
                     [:headers "Cache-Control"]))
          "/ must get no-cache")

      ;; Assertion 14
      (is (= "no-cache"
             (get-in (handler (make-req "/about"))
                     [:headers "Cache-Control"]))
          "/about must get no-cache"))))

;; ---------------------------------------------------------------------------
;; 3. wrap-security-headers merge behaviour
;; ---------------------------------------------------------------------------

(deftest security-headers-merge-test
  (testing "wrap-security-headers merges with existing headers, not replaces"
    (let [custom-handler (constantly {:status 200
                                     :headers {"X-Custom" "preserved"}
                                     :body "ok"})
          handler (wrap-security-headers custom-handler)
          resp    (handler (make-req "/"))]

      ;; Assertion 15 — custom header survives the merge
      (is (= "preserved" (get-in resp [:headers "X-Custom"]))
          "Existing X-Custom header must survive wrap-security-headers merge")

      ;; Assertion 16 — security headers were still added
      (is (= "DENY" (get-in resp [:headers "X-Frame-Options"]))
          "Security headers must still be injected alongside custom headers"))))

;; ---------------------------------------------------------------------------
;; REPL comment block
;; ---------------------------------------------------------------------------

(comment
  (clojure.test/run-tests 'hodari-gardens.server.middleware-test)
  )
