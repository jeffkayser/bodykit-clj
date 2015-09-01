(ns bodykit-clj.util
  (:require [clj-http.client :as http]))

;;;; Helper functions

(defn make-auth-server [key secret]
  (str "SecretPair accessKey=" key ",secret=" secret))

;; TODO: http://developer.bodylabs.com/authentication.html
(defn make-auth-app [key secret]
  (throw (UnsupportedOperationException. "ACL authentication scheme not supported")))

(defn make-auth [auth-mode key secret]
  {:authorization (case auth-mode
                    :server (make-auth-server key secret)
                    :app (make-auth-app key secret))})

(defn request [uri action params auth-mode key secret]
  (:body
    (http/post (str uri action)
               {:headers       (make-auth auth-mode key secret)
                :content-type  :json
                :save-request? true
                :debug-body    true
                :form-params   params})))

(defn measurement-params [params]
  (into
    {:scheme       "standard"
     :gender       "female"
     :size         "US 8"
     :unitSystem   "unitedStates"
     :measurements {:height              64.8
                    :weight              150
                    :bust_girth          37.7
                    :low_hip_girth       41
                    :waist_girth         31.5
                    :shirt_sleeve_length 30
                    :inseam              30}
     :generation   "2.0"}
    params))

(defn mesh-params [params]
  (measurement-params
    (into
      {:pose      "A"
       :meshFaces "tris"}
      params)))

(defn heatmap-params [params]
  (let [bodies (:bodies params)]
    (into {:bodies     [(mesh-params (first bodies))
                        (mesh-params (second bodies))]
           :generation "2.0"}
          (dissoc params :bodies))))
