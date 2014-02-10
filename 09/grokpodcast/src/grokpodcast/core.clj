(ns grokpodcast.core
  (:require [clojure.java.io        :as io]
            [clojure.pprint         :as pp]
            [clojure.string         :as str]
            [net.cgrand.enlive-html :as en])
  (:import  [java.net URL]))

(def file-dir "~/Music/grokpodcast/")

(defn- get-archive [url]
  (map
    #(str "http://www.grokpodcast.com" ((% :attrs) :href))
    (-> url
        URL.
        en/html-resource
        (en/select [:section#episode-archive :a]))))

(defn- get-file-link [episode-url]
  (let [link (-> episode-url
                 URL.
                 en/html-resource
                 (en/select [:section#podcast-infos :a])
                 first)]
    ((link :attrs) :href)))

(defn- download-file [episode-url]
  (let [link     (get-file-link episode-url)
        filename (-> link
                     (str/split #"/") 
                     last)
        filepath (str file-dir filename)]
    (when-not (. (io/file filepath) exists)
      (with-open [in  (io/input-stream link)
                  out (io/output-stream (str file-dir filename))]
        (println "Baixando" filename)
        (io/copy in out)
        (println "  Concluido" filename)))))

(defn -main [& args]
  (doall
    (pmap download-file (get-archive "http://www.grokpodcast.com/arquivo/")))
  (shutdown-agents))
