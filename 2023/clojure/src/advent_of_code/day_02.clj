(ns advent-of-code.day-02
  (:require [clojure.string :as string]))

(defn parse-set
  [set]
  (let [members (map string/trim (string/split set #","))
        members (map #(string/split % #" ") members)
        members (map #(vector (keyword (second %)) (read-string (first %))) members)]
    (into (hash-map) members)))

(defn parse-game
  [row]
  (let [[game sets] (string/split row #":")
        game-id (read-string (second (string/split game #" ")))
        sets (map string/trim (string/split sets #";"))
        sets (map parse-set sets)]
    {:game game-id
     :sets sets}))

(defn is-valid-set
  [{:keys [blue green red] :or {blue 0 green 0 red 0}}]
  (and (<= red 12) (<= green 13) (<= blue 14)))

(defn is-valid-game
  [{:keys [sets]}]
  (every? is-valid-set sets))

(defn part-1
  [input]
  (->> input
       (map parse-game)
       (filter is-valid-game)
       (map :game)
       (reduce +)))

(defn color-values-across-sets
  [color sets]
  (->> sets
       (map color)
       (filter #(not (nil? %)))))

(defn game-power
  [{:keys [sets]}]
  (->> [:red :green :blue]
       (map #(color-values-across-sets % sets))
       (map #(apply max %))
       (reduce *)))

(defn part-2
  [input]
  (->> input
       (map parse-game)
       (map game-power)
       (reduce +)))

(comment
  (parse-set "3 blue, 4 red")
  (def game (parse-game "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"))
  (game-power game))