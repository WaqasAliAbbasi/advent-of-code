(ns advent-of-code.day-01
  (:require [clojure.string :as string]))

(defn just-keep-numbers
  [s]
  (filter #(Character/isDigit %) s))

(defn part-1
  [input]
  (->> input
       string/split-lines
       (map just-keep-numbers)
       (map #(str (first %) (last %)))
       (map parse-double)
       (map int)
       (reduce +)))

(def letter-targets ["0" "1" "2" "3" "4" "5" "6" "7" "8" "9"])
(def word-letter-targets ["zero" "one" "two" "three" "four" "five" "six" "seven" "eight" "nine"])

(defn find-first-target
  [s]
  (let [letter-indexes (map #(vector (string/index-of s %1) %2) letter-targets (range))
        word-indexes (map #(vector (string/index-of s %1) %2) word-letter-targets (range))]
    (->> (concat letter-indexes word-indexes)
         (filter #(not (nil? (first %))))
         sort
         first
         second)))

(defn find-last-target
  [s]
  (let [letter-indexes (map #(vector (string/last-index-of s %1) %2) letter-targets (range))
        word-indexes (map #(vector (string/last-index-of s %1) %2) word-letter-targets (range))]
    (->> (concat letter-indexes word-indexes)
         (filter #(not (nil? (first %))))
         sort
         last
         second)))

(defn get-value
  [s]
  (-> (str (find-first-target s) (find-last-target s))
      parse-double
      int))

(defn part-2
  [input]
  (->> input
       string/split-lines
       (map get-value)
       (reduce +)))

(comment
  (find-last-target "two1nine"))