(ns hodari-gardens.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
            [cheshire.core :as json]))

(defn anti-forgery-meta-tag []
  [:meta {:name "csrf-token" :content *anti-forgery-token*}])

(def default-meta
  {:title "Hodari Gardens Resort | Luxury Accommodation & Events in Nakuru"
   :description "Experience serene luxury, premium events, and fine dining at Hodari Gardens Resort in Nakuru Lanet."
   :keywords "Hodari Gardens, Nakuru resort, accommodation, events venue, wedding venue, Nakuru Lanet"
   :og-title "Hodari Gardens Resort - Luxury & Events in Nakuru"
   :og-description "Experience serene luxury at Hodari Gardens. Accommodation, events, and dining in Nakuru Lanet."
   :og-image "/assets/images/og-image.jpg"})

(defn generate-json-ld
  "Generate Schema.org JSON-LD for the resort."
  [_metadata]
  (let [base-schema
        {"@context" "https://schema.org"
         "@type" ["Hotel" "LocalBusiness"]
         "name" "Hodari Gardens Resort"
         "image" "https://hodari-gardens.fly.dev/assets/images/hero-bg.jpg"
         "@id" "https://hodari-gardens.fly.dev"
         "url" "https://hodari-gardens.fly.dev"
         "telephone" "+254780693707"
         "address" {"@type" "PostalAddress"
                    "streetAddress" "Nakuru Lanet, Near Nakuru Specialist Hospital"
                    "addressLocality" "Nakuru"
                    "addressRegion" "Rift Valley"
                    "postalCode" "20100"
                    "addressCountry" "KE"}
         "geo" {"@type" "GeoCoordinates"
                "latitude" -0.2833
                "longitude" 36.0667}
         "priceRange" "$$"
         "amenityFeature" [{"@type" "LocationFeatureSpecification"
                            "name" "Free WiFi"
                            "value" true}
                           {"@type" "LocationFeatureSpecification"
                            "name" "Premium Gardens"
                            "value" true}]}]
    (json/generate-string base-schema)))

(defn hiccup-header [metadata]
  (let [meta (merge default-meta metadata)]
    [:head
     [:meta {:charset "UTF-8"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
     [:meta {:name "description" :content (:description meta)}]
     [:meta {:name "keywords" :content (:keywords meta)}]
     [:meta {:name "author" :content "Hodari Gardens Resort"}]

     ;; Open Graph / Facebook
     [:meta {:property "og:type" :content "website"}]
     [:meta {:property "og:url" :content "https://hodarigardens.co.ke/"}]
     [:meta {:property "og:title" :content (:og-title meta)}]
     [:meta {:property "og:description" :content (:og-description meta)}]
     [:meta {:property "og:image" :content (:og-image meta)}]

     ;; Twitter
     [:meta {:property "twitter:card" :content "summary_large_image"}]
     [:meta {:property "twitter:url" :content "https://hodarigardens.co.ke/"}]
     [:meta {:property "twitter:title" :content (:og-title meta)}]
     [:meta {:property "twitter:description" :content (:og-description meta)}]
     [:meta {:property "twitter:image" :content (:og-image meta)}]

     [:title (:title meta)]

     ;; Fonts
     [:link {:preconnect "https://fonts.googleapis.com"}]
     [:link {:preconnect "https://fonts.gstatic.com" :crossorigin "true"}]
     [:link {:href "https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=Playfair+Display:wght@400;600;700&display=swap"
             :rel "stylesheet"}]

     ;; Assets
     (include-css "/css/styles.css")
     [:link {:rel "icon" :type "image/png" :href "/assets/images/favicon.png"}]
     
     ;; Structured Data
     [:script {:type "application/ld+json"} (generate-json-ld metadata)]
     
     (anti-forgery-meta-tag)]))

(defn render-page [_request metadata]
  (html5
   {:lang "en"}
   (hiccup-header metadata)
   [:body.antialiased
    [:div#app
     ;; Initial SSR Shell for instant paint & SEO
     [:section {:class "relative h-screen flex items-center justify-center text-white bg-luxury-hero"}
      [:div {:class "text-center px-4"}
       [:h1 {:class "text-5xl md:text-8xl font-serif font-bold mb-6 text-premium-glow"} "Hodari Gardens Resort"]
       [:p {:class "text-xl md:text-3xl font-light tracking-widest uppercase text-premium-glow"} "Where Luxury Meets Nature"]
       [:p {:class "mt-8 text-gray-400 animate-pulse"} "Loading Excellence..."]]]]
    (include-js "/js/main.js")]))
