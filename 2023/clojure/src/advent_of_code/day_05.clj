(ns advent-of-code.day-05
  (:require [clojure.string :as string]
            [advent-of-code.utils :as utils]))

(defn parse-raw-map
  [{:keys [name values]}]
  (let [name (first (string/split name #" "))
        values (map utils/parse-numbers values)]
    (hash-map (keyword name) values)))

(defn parse-maps
  [input]
  (let [input (drop 2 input)
        input (filter not-empty input)
        add-value-to-map #(assoc %1 :values (conj (:values %1) %2))
        raw-maps (reduce (fn [acc row]
                           (if (string/includes? row "map")
                             (conj acc {:name (first (string/split row #" "))
                                        :values []})
                             (conj (drop 1 acc) (add-value-to-map (first acc) row)))) [] input)]
    (->> raw-maps
         (map parse-raw-map)
         (apply merge))))

(defn target->value
  [target map]
  (let [hit (some #(when (<= (nth % 1) target (+ (nth % 1) (nth % 2))) %) map)]
    (if (nil? hit) target
        (+ (first hit) (- target (second hit))))))

(defn find-location
  [maps seed]
  (-> seed
      (target->value (:seed-to-soil maps))
      (target->value (:soil-to-fertilizer maps))
      (target->value (:fertilizer-to-water maps))
      (target->value (:water-to-light maps))
      (target->value (:light-to-temperature maps))
      (target->value (:temperature-to-humidity maps))
      (target->value (:humidity-to-location maps))))

(defn part-1
  [input]
  (let [seeds (first input)
        seeds (second (string/split seeds #":"))
        seeds (utils/parse-numbers seeds)
        maps  (parse-maps input)]
    (->> seeds
         (map #(find-location maps %))
         (apply min))))

(defn get-overlap
  [left right offset]
  (let [overlap [(max (first left) (first right)) (min (second left) (second right))]]
    (if (apply > overlap) nil [(+ (first overlap) offset) (+ (second overlap) offset)])))

(defn get-non-overlap
  [interval overlap]
  (if (nil? overlap) [interval]
      (let [left (when (< (first interval) (first overlap)) [(first interval) (dec (first overlap))])
            right (when (> (second interval) (second overlap)) [(inc (second overlap)) (second interval)])]
        (filter seq [left right]))))

(defn intersect-intervals
  [from targets]
  (reduce (fn [{:keys [from no-offset overlaps]} [current offset]]
            (hash-map :from (mapcat #(get-non-overlap % (get-overlap % current 0)) from)
                      :no-offset (concat no-offset (filter seq (map #(get-overlap % current 0) from)))
                      :overlaps (concat overlaps (filter seq (map #(get-overlap % current offset) from))))) {:from from
                                                                                                             :no-offset []
                                                                                                             :overlaps []} targets))

(defn target-interval->value
  [target map-values]
  (let [source-intervals (map #(list (list (nth % 1) (+ (nth % 1) (dec (nth % 2)))) (- (nth % 0) (nth % 1)))  map-values)
        intersect        (intersect-intervals target source-intervals)]
    (concat (:overlaps intersect) (:from intersect))))

(defn find-location-interval
  [maps seed]
  (-> seed
      (target-interval->value (:seed-to-soil maps))
      (target-interval->value (:soil-to-fertilizer maps))
      (target-interval->value (:fertilizer-to-water maps))
      (target-interval->value (:water-to-light maps))
      (target-interval->value (:light-to-temperature maps))
      (target-interval->value (:temperature-to-humidity maps))
      (target-interval->value (:humidity-to-location maps))))

(defn part-2
  [input]
  (let [seeds (first input)
        seeds (second (string/split seeds #":"))
        seeds (utils/parse-numbers seeds)
        seeds (partition 2 seeds)
        seeds (map #(list (first %) (+ (first %) (dec (second %)))) seeds)
        maps (parse-maps input)]
    (->> (find-location-interval maps seeds)
         sort
         first
         first)))