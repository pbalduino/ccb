(ns autores.core
  (:require [crouton.html   :as html]
            [clojure.pprint :as pp]
            [clojure.walk   :as walk]))

(defn- read-link [node]
  (let [link ((node :attrs) :href)]
    link))

(defn- trasverse [node]
  (cond (map? node)
          (if (= :a (node :tag))
            (read-link node)
            (trasverse (node :content)))
        (vector? node)
          (map trasverse node)
        :else node))

(defn- remove-spaces [dom]
  (filter map? (dom :content)))

(defn- get-body [dom]
  (first 
    (filter #(= :body (% :tag)) 
            (remove-spaces dom))))

(defn- get-section [dom]
  (first
    (filter #(= :section (% :tag))
            (remove-spaces dom))))

(defn -main [& args]
  (let [section (-> "http://www.casadocodigo.com.br"
                 html/parse
                 get-body
                 get-section)]
    (println (walk/prewalk trasverse section))))