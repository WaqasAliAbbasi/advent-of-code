(ns advent-of-code.day-06
  (:require [clojure.string :as string]))

(defn split-and-trim
  [s d]
  (filter not-empty (map string/trim (string/split s d))))

(defn parse-races
  [input]
  (let [times (-> input
                  first
                  (split-and-trim #":")
                  second
                  (split-and-trim #" ")
                  (#(map read-string %)))
        distances (-> input
                      second
                      (split-and-trim #":")
                      second
                      (split-and-trim #" ")
                      (#(map read-string %)))]
    (map vector times distances)))

(defn get-race
  [[time distance]]
  (->> (range 0 (inc time))
       (map #(* % (- time %)))
       (filter #(> % distance))
       count))

(defn part-1
  [input]
  (->> input
       parse-races
       (map get-race)
       (reduce *)))

(defn parse-races-kerned
  [input]
  (let [times (-> input
                  first
                  (split-and-trim #":")
                  second
                  (split-and-trim #" ")
                  (#(string/join #"" %))
                  read-string)
        distances (-> input
                      second
                      (split-and-trim #":")
                      second
                      (split-and-trim #" ")
                      (#(string/join #"" %))
                      read-string)]
    (vector (vector times distances))))

(defn part-2
  [input]
  (->> input
       parse-races-kerned
       (map get-race)
       (reduce *)))