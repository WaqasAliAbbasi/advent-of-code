(ns advent-of-code.day-18
  (:require [clojure.string :as string]
            [clojure.core.matrix :as matrix]))

(defn apply-step
  [[row col] [step amount]]
  (case step
    "R" [row (+ col amount)]
    "D" [(+ row amount) col]
    "L" [row (- col amount)]
    "U" [(- row amount) col]))

(defn coordinates
  [steps]
  (-> (reduce #(conj %1 (apply-step (last %1) %2)) [[0 0]] steps)
      drop-last))

(defn roll
  [xs]
  (cons (last xs) (drop-last xs)))

(defn get-area
  [parsed]
  (let [path (coordinates parsed)
        path-length (reduce + (map second parsed))
        x    (map second path)
        y    (map first path)
        A    (* 0.5 (abs (- (matrix/dot x (roll y)) (matrix/dot y (roll x)))))]
    (+ (- (+ A 1) (/ path-length 2)) path-length)))

(defn parse-part1
  [input]
  (let [parse-row (fn [row] (let [[step amount] (string/split row #" ")
                                  amount        (read-string amount)]
                              [step amount]))]
    (map parse-row input)))

(defn part-1
  [input]
  (-> input parse-part1 get-area))

(defn parse-part2
  [input]
  (let [parse-row (fn [row] (let [[_ hex] (string/split row #"#")
                                  amount  (subs hex 0 (-> hex count dec dec))
                                  amount  (Integer/parseInt amount 16)
                                  step    (nth hex (-> hex count dec dec))
                                  step    (read-string (str step))
                                  step    (nth ["R" "D" "L" "U"] step)]
                              [step amount]))]
    (map parse-row input)))

(defn part-2
  [input]
  (-> input
      parse-part2
      get-area))