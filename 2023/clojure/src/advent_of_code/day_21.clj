(ns advent-of-code.day-21
  (:require [advent-of-code.utils :as utils]))

(defn in-bounds
  [[max-row max-col] [row col]]
  (and (< -1 row max-row)
       (< -1 col max-col)))

(defn get-value
  [input [max-row max-col] [row col]]
  (-> input
      (nth (mod row max-row))
      (nth (mod col max-col))))

(defn get-next
  [graph size current]
  (->> [[0 1] [0 -1] [1 0] [-1 0]]
       (map #(map + current %))
       #_(filter #(in-bounds size %))
       (filter #(contains? #{\. \S} (get-value graph size %)))))

(defn travel
  [graph size start moves]
  (loop [frontier #{start}
         acc      0
         quadratic-hits []]
    (when (= 0 (mod (- acc 65) (first size))) (println acc (count frontier)))
    (if (or (= acc moves) (= 3 (count quadratic-hits))) (count frontier)
        (let [new-frontier (set (mapcat #(get-next graph size %) frontier))]
          (recur new-frontier (inc acc) (if (= 0 (mod (- acc 65) (first size))) (conj quadratic-hits acc) quadratic-hits))))))

(defn part-1
  [input]
  (let [graph   (map seq input)
        size    [(count input) (count (first input))]
        [s]     (filter #(= (first %) \S) (utils/parse-2d-string input))]
    (travel graph size (second s) 64)))

;; use three points with quadratic formula fit on wolfram
(defn part-2 [input]
  (let [graph   (map seq input)
        size    [(count input) (count (first input))]
        [s]     (filter #(= (first %) \S) (utils/parse-2d-string input))]
    (travel graph size (second s) 1000)))