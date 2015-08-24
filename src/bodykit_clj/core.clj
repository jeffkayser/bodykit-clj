(ns bodykit-clj.core
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clj-http.client :as http]))

(def ^:dynamic *key* nil)
(def ^:dynamic *secret* nil)

(defn request
  [])
