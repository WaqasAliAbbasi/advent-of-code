(ns core
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [advent-of-code.day-01]
            [advent-of-code.day-02]
            [advent-of-code.day-03]
            [advent-of-code.day-04]
            [advent-of-code.day-05]
            [advent-of-code.day-09]))

(defn read-input
  [path]
  (-> path
      io/resource
      slurp
      string/split-lines))

(defn -main [{:keys [day]}]
  (let [day (str day)]
    (case day
      "d01.p1" (println (advent-of-code.day-01/part-1 (read-input day)))
      "d01.p2" (println (advent-of-code.day-01/part-2 (read-input day)))
      "d02.p1" (println (advent-of-code.day-02/part-1 (read-input day)))
      "d02.p2" (println (advent-of-code.day-02/part-2 (read-input day)))
      "d03.p1" (println (advent-of-code.day-03/part-1 (read-input day)))
      "d03.p2" (println (advent-of-code.day-03/part-2 (read-input day)))
      "d04.p1" (println (advent-of-code.day-04/part-1 (read-input day)))
      "d04.p2" (println (advent-of-code.day-04/part-2 (read-input day)))
      "d05.p1" (println (advent-of-code.day-05/part-1 (read-input day)))
      "d05.p2" (println (advent-of-code.day-05/part-2 (read-input day)))
      "d09.p1" (println (advent-of-code.day-09/part-1 (read-input day)))
      "d09.p2" (println (advent-of-code.day-09/part-2 (read-input day)))
      "default")))