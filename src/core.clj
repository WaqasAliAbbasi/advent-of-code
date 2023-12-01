(ns core
  (:require [clojure.java.io :as io]
            [advent-of-code.day-01]
            [advent-of-code.day-02]))

(defn read-input
  [path]
  (slurp (io/resource path)))

(defn -main [{:keys [day]}]
  (let [day (str day)]
    (case day
      "d01.p1" (println (advent-of-code.day-01/part-1 (read-input day)))
      "d01.p2" (println (advent-of-code.day-01/part-2 (read-input day)))
      "d02.p1" (println (advent-of-code.day-02/part-1 (read-input day)))
      "d02.p2" (println (advent-of-code.day-02/part-2 (read-input day)))
      "default")))