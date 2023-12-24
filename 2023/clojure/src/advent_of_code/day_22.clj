(ns advent-of-code.day-22
  (:require [clojure.string :as str]
            [advent-of-code.utils :as utils]
            [clojure.set :as set]))

(defn parse-row
  [row]
  (let [[start end] (str/split row #"~")
        start (utils/parse-numbers start #",")
        end (utils/parse-numbers end #",")]
    [start end]))

(defn parse
  [input]
  (map parse-row input))

(defn range-cube
  [[[sx sy sz] [ex ey ez]]]
  (set (for [x (range sx (inc ex))
             y (range sy (inc ey))
             z (range sz (inc ez))]
         [x y z])))

(defn reduce-height
  [[[sx sy sz] [ex ey ez]]]
  [[sx sy (dec sz)] [ex ey (dec ez)]])

(defn new-cube
  [space cube]
  (loop [c cube]
    (let [lowered (reduce-height c)
          lowered-space (range-cube lowered)]
      (if (or (= 1 (-> c first (nth 2))) (not-empty (set/intersection space lowered-space))) c
          (recur lowered)))))

(defn compress-cube
  [[space cubes] cube]
  (let [without-cube (set/difference space (range-cube cube))
        new-cube     (new-cube without-cube cube)
        with-new     (set/union without-cube (range-cube new-cube))]
    [with-new (conj cubes new-cube)]))

(defn compress-cubes
  [space cubes]
  (reduce compress-cube [space []] cubes))

(defn can-disintegrate?
  [space cubes cube]
  (let [except-current-cube (disj cubes cube)
        without-cube (set/difference space (range-cube cube))]
    (some #(not= % (new-cube (set/difference without-cube (range-cube %)) %)) except-current-cube)))

(defn part-1 [input]
  (let [cubes (sort-by #(-> % first (nth 2)) (parse input))
        space-covered (reduce (fn [acc cube] (reduce #(conj %1 %2) acc (range-cube cube))) #{} cubes)
        [compressed cubes] (compress-cubes space-covered cubes)
        cubes (set cubes)
        cannot-disintegrate (filter #(not (can-disintegrate? compressed cubes %)) cubes)]
    (count cannot-disintegrate)))

(defn count-disintegrations
  [space cubes cube]
  (let [except-current-cube (filter #(not= % cube) cubes)
        without-cube (set/difference space (range-cube cube))
        [_ disintegrations] (reduce (fn [[state count] c]
                                      (let [temp-state (set/difference state (range-cube c))
                                            new-cube (new-cube temp-state c)
                                            new-state (set/union temp-state (range-cube new-cube))
                                            new-count (if (not= c new-cube) (inc count) count)]
                                        [new-state new-count])) [without-cube 0] except-current-cube)]
    disintegrations))

(defn part-2 [input]
  (let [cubes (sort-by #(-> % first (nth 2)) (parse input))
        space-covered (reduce (fn [acc cube] (reduce #(conj %1 %2) acc (range-cube cube))) #{} cubes)
        [compressed cubes] (compress-cubes space-covered cubes)]
    (reduce + (map #(count-disintegrations compressed cubes %) cubes))))