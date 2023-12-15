(ns advent-of-code.day-13
  (:require [clojure.string :as string]
            [advent-of-code.utils :as utils]))

(defn process-pattern
  [raw-pattern]
  (->> (string/split-lines raw-pattern)
       (map seq)
       (map #(into (vector) %))
       (into (vector))))

(defn parse
  [input]
  (->> (string/split input #"\r?\n\r?\n")
       (map process-pattern)))

(defn errs [a b] (apply + (map #(if (= %1 %2) 0 1) a b)))

(defn is-reflected
  [n diff xs]
  (let [[a b] (split-at n xs)]
    (when (= diff (apply + (map errs (reverse a) b))) n)))

(defn hor
  [pattern diff]
  (let [transposed (utils/transpose pattern)]
    (some #(is-reflected % diff transposed) (range 1 (count transposed)))))

(defn ver
  [pattern diff]
  (when-let [result (some #(is-reflected % diff pattern) (range 1 (count pattern)))]
    (* 100 result)))

(defn reflect
  [diff pattern]
  (or (hor pattern diff) (ver pattern diff)))

(defn part-1
  [input]
  (->> (parse input)
       (map (partial reflect 0))
       (reduce +)))

(defn part-2
  [input]
  (->> (parse input)
       (map (partial reflect 1))
       (reduce +)))