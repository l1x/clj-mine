(ns clj-mine.core
  (:require [clojure.string         :as string]
            [net.cgrand.enlive-html :as html])
  (:use     [incanter.core])
  (:gen-class))

(defn- get-category
  "Takes an article and return the category name"
  [article]
  ;("Cars") -> "Cars"
  (string/join
    ;({:tag :h2, :attrs {:id "cars"}, :content ("Cars")}) -> ("Cars")
    (map html/text (html/select article [:header :h2]))))

(defn- get-car
  "Takes li item and return a map of the car's name and type"
  [li]
  (let [[{names :content} rel] (:content li)]
    {:name (apply str names)
     :type (string/trim rel)}))

(defn- get-rows 
  "This takes an article and returns the cars"
  [article]
   (let [category (get-category article)]
     (map #(assoc % :category category)
          (map get-car
               (html/select article [:ul :li])))))

(defn- load-data
  "Loads sample data"
  [file]
  (let [html (html/html-resource (clojure.java.io/file file))
        articles (html/select html [:article])]
    (to-dataset (mapcat get-rows articles))))
                              
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (load-data "sample/cars.html"))


