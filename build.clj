(ns build
  "Build script for Hodari Gardens Resort application."
  (:require [clojure.tools.build.api :as b]))

(def lib 'hodari-gardens/hodari-gardens)
(def version "1.0.0")
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def uber-file (format "target/%s-%s.jar" (name lib) version))

(defn clean
  "Remove build artifacts."
  [_]
  (b/delete {:path "target"}))

(defn uber
  "Build uberjar for production deployment."
  [_]
  (clean nil)
  (b/copy-dir {:src-dirs ["src/clj" "src/cljs" "resources"]
               :target-dir class-dir})
  (b/compile-clj {:basis basis
                  :src-dirs ["src/clj"]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis basis
           :main 'hodari-gardens.server})
  (println (str "Uberjar created: " uber-file)))
