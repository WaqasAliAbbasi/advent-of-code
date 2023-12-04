(ns advent-of-code.day-04
  (:require [clojure.string :as string]
            [clojure.set :as set]))

(defn split-and-trim
  [s d]
  (filter not-empty (map string/trim (string/split s d))))

(defn parse-card
  [row]
  (let [[card numbers]    (split-and-trim row #":")
        card              (read-string (second (split-and-trim card #" ")))
        [winning current] (split-and-trim numbers #"\|")
        winning           (map read-string (split-and-trim winning #" "))
        current           (map read-string (split-and-trim current #" "))]
    {:card card
     :winning winning
     :current current}))

(defn get-winning-count
  [{:keys [winning current]}]
  (count (set/intersection (set winning) (set current))))

(defn get-points
  [card]
  (let [winning-count (get-winning-count card)]
    (if (pos? winning-count) (int (Math/pow 2 (dec winning-count)))
        0)))

(defn part-1
  [input]
  (->> input
       (map parse-card)
       (map get-points)
       (reduce +)))

(def get-card-count
  (memoize (fn [card cards]
             (let [current-card-id  (:card card)
                   winning-count    (get-winning-count card)
                   next-cards       (range (inc current-card-id) (+ current-card-id winning-count 1))
                   next-cards       (map #(nth cards (dec %)) next-cards)
                   next-card-points (map #(get-card-count % cards) next-cards)
                   next-card-points (reduce + next-card-points)]
               (+ 1 next-card-points)))))

(defn part-2
  [input]
  (let [cards (map parse-card input)]
    (->> cards
         (map #(get-card-count % cards))
         (reduce +))))