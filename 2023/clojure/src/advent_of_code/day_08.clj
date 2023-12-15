(ns advent-of-code.day-08
  (:require [clojure.string :as string]
            [advent-of-code.utils :as utils]
            [clojure.math.numeric-tower :as math]))

(defn parse-map
  [row]
  (let [[from to] (utils/split-and-trim row #"=")
        to (subs to 1 (dec (count to)))
        [left right] (utils/split-and-trim to #",")]
    [from {:left left :right right}]))


(defn parse-maps
  [maps]
  (->> maps
       (map parse-map)
       (into {})))

(defn parse-steps
  [steps]
  (->> (string/split steps #"")
       (map #(if (= % "L") :left :right))))

(defn parse
  [[steps _ & maps]]
  {:steps (parse-steps steps)
   :maps (parse-maps maps)})

(defn find-next
  [current acc {:keys [steps maps]}]
  (let [next (nth steps (mod acc (count steps)))]
    (-> maps
        (get current)
        next)))

(defn follow
  [start end-check game]
  (loop [current start
         acc     0]
    (if (end-check current) acc
        (recur (find-next current acc game) (inc acc)))))

(defn part-1
  [input]
  (->> input
       parse
       (follow "AAA" #(= % "ZZZ"))))

(defn part-2
  [input]
  (let [game (parse input)
        end-check #(= (last %) \Z)]
    (->> game
         :maps
         keys
         (filter #(= (last %) \A))
         (map #(follow % end-check game))
         (reduce math/lcm))))