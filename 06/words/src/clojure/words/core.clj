(ns words.core
  (:import [java.nio.file FileSystems Path StandardWatchEventKinds]
           [watcher Watcher]))

(defn strip [^String text ^String pattern]
    (.replaceAll text pattern ""))

(defn remove-line-breaks [^String text]
  (strip text "\n"))

(defn remove-spaces [^String text]
  (-> text 
      (strip " ")
      (strip ",")
      (strip "\\.")))

(defn remove-tags [^String text]
  (-> text
      (strip "<title>.*</title>")
      (strip "<head>.*</head>")
      (strip "<!.*>")
      (strip "<\\w*>")
      (strip "</\\w*>")
      (strip "<.*>")))

(defn count-characters [^String text]
  (let [stripped (-> text
                     remove-tags
                     remove-spaces
                     remove-line-breaks)]
    ; (println stripped)
    (count stripped)))

(defn do-count [filename]
  (let [text          (slurp filename)
        total-size    (count text)
        stripped-size (count-characters text)]
      (println (str "Tamanho total: " total-size " bytes"))
      (println (str "Tamanho real : " stripped-size " bytes"))
      (println (str "Progresso    : " (format "%,.1f" (* (/ stripped-size 20000.0) 100)) "%"))))

(def watcher (. (FileSystems/getDefault) newWatchService))

(defn get-path [^String filename]
   (let [fs (FileSystems/getDefault)]
      (. fs getPath "." (into-array [filename]))))

(defn watch-file-change [filename what]
  (let [path (get-path filename)
        key  (. path register watcher (into-array [StandardWatchEventKinds/ENTRY_MODIFY]))]
    (println key)))

(defn -main [& args]
  (if (seq? args)
    (let [filename (first args)]
      (do-count (first args)))
    (println "Informe o nome do arquivo HTML"))
  (println))