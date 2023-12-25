(ns core
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [advent-of-code.day-01]
            [advent-of-code.day-02]
            [advent-of-code.day-03]
            [advent-of-code.day-04]
            [advent-of-code.day-05]
            [advent-of-code.day-06]
            [advent-of-code.day-07]
            [advent-of-code.day-08]
            [advent-of-code.day-09]
            [advent-of-code.day-10]
            [advent-of-code.day-11]
            [advent-of-code.day-12]
            [advent-of-code.day-13]
            [advent-of-code.day-14]
            [advent-of-code.day-15]
            [advent-of-code.day-16]
            [advent-of-code.day-17]
            [advent-of-code.day-18]
            [advent-of-code.day-19]
            [advent-of-code.day-20]
            [advent-of-code.day-21]
            [advent-of-code.day-22]
            [advent-of-code.day-23]
            [advent-of-code.day-24]
            [advent-of-code.day-25]))

(defn read-input
  [path]
  (-> path
      io/resource
      slurp
      string/split-lines))

(defn read-input-raw
  [path]
  (-> path
      io/resource
      slurp))

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
      "d06.p1" (println (advent-of-code.day-06/part-1 (read-input day)))
      "d06.p2" (println (advent-of-code.day-06/part-2 (read-input day)))
      "d07.p1" (println (advent-of-code.day-07/part-1 (read-input day)))
      "d07.p2" (println (advent-of-code.day-07/part-2 (read-input day)))
      "d08.p1" (println (advent-of-code.day-08/part-1 (read-input day)))
      "d08.p2" (println (advent-of-code.day-08/part-2 (read-input day)))
      "d09.p1" (println (advent-of-code.day-09/part-1 (read-input day)))
      "d09.p2" (println (advent-of-code.day-09/part-2 (read-input day)))
      "d10.p1" (println (advent-of-code.day-10/part-1 (read-input day)))
      "d10.p2" (println (advent-of-code.day-10/part-2 (read-input day)))
      "d11.p1" (println (advent-of-code.day-11/part-1 (read-input day)))
      "d11.p2" (println (advent-of-code.day-11/part-2 (read-input day)))
      "d12.p1" (println (advent-of-code.day-12/part-1 (read-input day)))
      "d12.p2" (println (advent-of-code.day-12/part-2 (read-input day)))
      "d13.p1" (println (advent-of-code.day-13/part-1 (read-input-raw day)))
      "d13.p2" (println (advent-of-code.day-13/part-2 (read-input-raw day)))
      "d14.p1" (println (advent-of-code.day-14/part-1 (read-input day)))
      "d14.p2" (println (advent-of-code.day-14/part-2 (read-input day)))
      "d15.p1" (println (advent-of-code.day-15/part-1 (read-input day)))
      "d15.p2" (println (advent-of-code.day-15/part-2 (read-input day)))
      "d16.p1" (println (advent-of-code.day-16/part-1 (read-input day)))
      "d16.p2" (println (advent-of-code.day-16/part-2 (read-input day)))
      "d17.p1" (println (advent-of-code.day-17/part-1 (read-input day)))
      "d17.p2" (println (advent-of-code.day-17/part-2 (read-input day)))
      "d18.p1" (println (advent-of-code.day-18/part-1 (read-input day)))
      "d18.p2" (println (advent-of-code.day-18/part-2 (read-input day)))
      "d19.p1" (println (advent-of-code.day-19/part-1 (read-input day)))
      "d19.p2" (println (advent-of-code.day-19/part-2 (read-input day)))
      "d20.p1" (println (advent-of-code.day-20/part-1 (read-input day)))
      "d20.p2" (println (advent-of-code.day-20/part-2 (read-input day)))
      "d21.p1" (println (advent-of-code.day-21/part-1 (read-input day)))
      "d21.p2" (println (advent-of-code.day-21/part-2 (read-input day)))
      "d22.p1" (println (advent-of-code.day-22/part-1 (read-input day)))
      "d22.p2" (println (advent-of-code.day-22/part-2 (read-input day)))
      "d23.p1" (println (advent-of-code.day-23/part-1 (read-input day)))
      "d23.p2" (println (advent-of-code.day-23/part-2 (read-input day)))
      "d24.p1" (println (advent-of-code.day-24/part-1 (read-input day)))
      "d24.p2" (println (advent-of-code.day-24/part-2 (read-input day)))
      "d25.p1" (println (advent-of-code.day-25/part-1 (read-input day)))
      "default")))