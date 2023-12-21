(ns advent-of-code.utils
  (:require [clojure.string :as string]))

(defn split-and-trim
  [s d]
  (filter not-empty (map string/trim (string/split s d))))

(defn parse-numbers
  ([row]
   (parse-numbers row #" "))
  ([row d]
   (map read-string (split-and-trim row d))))

(defn transpose [m]
  (when-not (nil? m)
    (apply mapv vector m)))

(defn parse-2d-string
  [input]
  (->> input
       (map seq)
       (mapcat (fn [row xs] (map #(list %2 [row %1]) (range) xs)) (range))))