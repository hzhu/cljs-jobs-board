(ns main.helper-functions
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as str]
            ))

;; Turns epoch into human readable date
(defn make-date [epoch]
  (subs (.toDateString (js/Date. epoch)) 4 10))

;; Given value, returns a valid email or nil
(defn regex-email? [value]
  (re-matches #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?" value))
       
;; If string is email, returns an anchor tag.
(defn email-to-html [string]
    (if (nil? (regex-email? string))
      (str string)
      (str "<a href=\"mailto:" string "\">" string "</a>")))

;; Returns original string with HTML transformed emails
(defn transform-email [string]
   (let [splitted (str/split string #" ")]
     (println splitted)
     (str/join " " (vec (map email-to-html splitted)))
   ))
