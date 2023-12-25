(ns advent-of-code.day-24
  (:require [clojure.string :as str]
            [advent-of-code.utils :as utils]
            [clojure.math.combinatorics :as combo]))

(defn parse
  [input]
  (let [parse-row (fn [row]
                    (let [[position velocity] (str/split row #"@")
                          position (utils/parse-numbers position #",")
                          velocity (utils/parse-numbers velocity #",")] [position velocity]))]
    (map parse-row input)))

(defn find-line
  [[[px py _] [vx vy _]]]
  (let [m (/ vy vx)
        c (- py (* px m))]
    [m c]))

(defn find-intersection
  [[a b]]
  (let [[m1 c1] (find-line a)
        [m2 c2] (find-line b)
        top (- c2 c1)
        bottom (- m1 m2)
        x (if (= 0 bottom) nil (/ top bottom))
        y (if (nil? x) nil (+ (* m1 x) c1))]
    [x y [a b]]))

(defn within-range
  [min max value]
  (<= min value max))

(defn in-future?
  [x y [[px py _] [vx vy]]]
  (and (pos? (/ (- x px) vx))
       (pos? (/ (- y py) vy))))

(defn in-future-intersection?
  [[x y [a b]]]
  (and (in-future? x y a) (in-future? x y b)))

(defn part-1 [input]
  (let [min-range 200000000000000
        max-range 400000000000000
        parsed (parse input)
        comb (combo/combinations parsed 2)
        intersections (->> comb
                           (map find-intersection)
                           (filter #(not (nil? (first %))))
                           (filter #(and (within-range min-range max-range (first %)) (within-range min-range max-range (second %)))))]
    (-> (filter in-future-intersection? intersections)
        count)))

(defn part-2 [input] input)