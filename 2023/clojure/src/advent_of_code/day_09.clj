(ns advent-of-code.day-09
  (:require [clojure.string :as string]))

(defn parse-numbers
  [row]
  (map read-string (string/split row #" ")))

(defn extrapolate
  [start-acc next-acc xs]
  (loop [numbers xs
         acc start-acc]
    (if (= 0 (reduce + numbers))
      acc
      (recur (map #(- %2 %1) numbers (drop 1 numbers)) (next-acc acc numbers)))))

(defn part-1
  [input]
  (->> input
       (map parse-numbers)
       (map (partial extrapolate 0 #(+ %1 (last %2))))
       (reduce +)))

(defn part-2
  [input]
  (->> input
       (map parse-numbers)
       (map (partial extrapolate [1 0] (fn [[sign total] xs] [(* -1 sign) (+ total (* sign (first xs)))])))
       (map second)
       (reduce +)))