(ns advent-of-code.day-11
  (:require [clojure.math.combinatorics :as combo]))

(defn is-dots
  [row]
  (every? #(= % \.) row))

(defn get-column
  [input n]
  (map #(nth % n) input))

(defn get-galaxy-coordinates
  [input]
  (->> (for [y (range (count input))
             x (range (count (first input)))]
         (when (= (nth (nth input y) x) \#) [x y]))
       (filter not-empty)))

(defn manhattan
  [x-expand y-expand n [a b]]
  (let [distance (+ (abs (- (first a) (first b))) (abs (- (second a) (second b))))
        [x-start x-end] (sort [(first a) (first b)])
        [y-start y-end] (sort [(second a) (second b)])
        x-intersects    (filter #(contains? x-expand %) (range x-start (inc x-end)))
        y-intersects    (filter #(contains? y-expand %) (range y-start (inc y-end)))]
    (+ distance (* (count x-intersects) n) (* (count y-intersects) n))))

(defn part-1
  [input]
  (let [x-expand (set (filter #(is-dots (get-column input %)) (range (count (first input)))))
        y-expand (set (filter #(is-dots (nth input %)) (range (count input))))]
    (-> input
        get-galaxy-coordinates
        (combo/combinations 2)
        (#(map (partial manhattan x-expand y-expand 1) %))
        (#(reduce + %)))))

(defn part-2
  [input]
  (let [x-expand (set (filter #(is-dots (get-column input %)) (range (count (first input)))))
        y-expand (set (filter #(is-dots (nth input %)) (range (count input))))]
    (-> input
        get-galaxy-coordinates
        (combo/combinations 2)
        (#(map (partial manhattan x-expand y-expand (dec 1000000)) %))
        (#(reduce + %)))))