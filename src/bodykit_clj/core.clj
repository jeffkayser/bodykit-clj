(ns bodykit-clj.core
  (:require [cheshire.core :as json]
            [bodykit-clj.util :as u]))

;;;; API tokens

;; Authentication modes described here:
;; http://developer.bodylabs.com/authentication.html
(def ^:dynamic *auth-mode* :server)
(def ^:dynamic *key* nil)
(def ^:dynamic *secret* nil)

(def ^:dynamic *base-uri* "https://api.bodylabs.com/instant/")


;;;; API

(defmacro with-auth
  "Sets authentication info for BodyKit API requests."
  [auth-mode key secret & body]
  `(binding [*auth-mode* ~auth-mode
             *key* ~key
             *secret* ~secret]
     (do
       ~@body)))

;; http://developer.bodylabs.com/instant_api_reference.html#Measurements
(defn measurements
  "Calls the 'measurements' method, which predicts over 50 measurements
  from a smaller set of input measurements. Returns a map."
  [params]
  (clojure.walk/keywordize-keys
    (json/decode
      (let [req-params (u/measurement-params params)]
        (u/request *base-uri* "measurements" req-params *auth-mode* *key* *secret*)))))

;; http://developer.bodylabs.com/instant_api_reference.html#Mesh
(defn mesh
  "Calls the 'mesh' method, which obtains a 3D mesh of the predicted
  body shape from a given set of input measurements. Returns a string
  containing a Wavefront OBJ file."
  [params]
  (let [req-params (u/mesh-params params)]
    (u/request *base-uri* "mesh" req-params *auth-mode* *key* *secret*)))

;; http://developer.bodylabs.com/instant_api_reference.html#Mesh-metrics
(defn mesh-metrics
  "Calls the 'mesh_metrics' method, which returns metrics about the
  mesh predicted for the given set of measurements. Returns a map."
  [params]
  (clojure.walk/keywordize-keys
    (json/decode
      (let [req-params (u/measurement-params params)]
        (u/request *base-uri* "mesh_metrics" req-params *auth-mode* *key* *secret*)))))

;; http://developer.bodylabs.com/instant_api_reference.html#HeatMap
(defn heatmap
  "Calls the 'heatmap' method, which create heatmap comparison meshes of one
  body to another. Returns a string containing a Wavefront OBJ file, whose
  vertices also contain RGB values."
  [params]
  (let [req-params (u/heatmap-params params)]
    (u/request *base-uri* "heatmap" req-params *auth-mode* *key* *secret*)))
