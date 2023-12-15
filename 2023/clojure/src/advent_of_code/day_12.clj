(ns advent-of-code.day-12
  (:require [clojure.string :as string]
            [advent-of-code.utils :as utils]))

(defn parse
  [repeat-times row]
  (let [[records target] (string/split row #" ")
        records          (string/join "?" (take repeat-times (repeat records)))
        target           (utils/parse-numbers target #",")
        target           (apply concat (take repeat-times (repeat target)))]
    [records target]))

(defn valid-suffixes [row number]
  (for [i (range (inc (- (count row) number)))
        :while (every? #{\. \?} (take i row))
        :when (every? #{\# \?} (take number (drop i row)))
        :when (#{\. \?} (nth row (+ i number) \.))]
    (drop (+ i number 1) row)))

(def acs
  (memoize (fn [row numbers]
             (if-let [[n & nrs] (seq numbers)]
               (reduce + (for [s (valid-suffixes row n)] (acs s nrs)))
               (if (every? #{\. \?} row) 1 0)))))

(defn part-1
  [input]
  (->> input
       (map (partial parse 1))
       (map (partial apply acs))
       (reduce +)))

(defn part-2
  [input]
  (->> input
       (map (partial parse 5))
       (map (partial apply acs))
       (reduce +)))