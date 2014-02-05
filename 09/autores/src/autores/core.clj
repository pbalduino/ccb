(ns autores.core
  (:require [clojure.pprint :as pp]
            [net.cgrand.enlive-html :as en]
            [clojure.string :as str])
  (:import  [java.net URL]))

(defn- get-links [url]
  (let [links (-> url
                  URL. 
                  en/html-resource
                  (en/select [:body :section :a]))]
      (filter
        #(. % contains "livro")
        (map #(str url ((% :attrs) :href)) 
             links))))

(defn- get-author [url]
  (let [authors (-> url
                    URL.
                    en/html-resource
                    (en/select [:span.product-author-link]))]
    (str/split
      (str/replace
        (first
          ((first authors) :content))
        #"(\n|  )" "")
      #"(, | e )")))

(defn -main [& args]
  (pp/pprint
    (reverse 
      (sort-by last
        (frequencies
          (flatten
            (pmap get-author 
                 (get-links "http://www.casadocodigo.com.br")))))))
  (shutdown-agents))