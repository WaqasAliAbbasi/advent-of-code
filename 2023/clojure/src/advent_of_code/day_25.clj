(ns advent-of-code.day-25
  (:require [clojure.string :as str]
            [advent-of-code.utils :as utils]
            [clojure.set :as set]))

(defn parse
  [input]
  (let [parse-row (fn [row] (let [[from to] (str/split row #":")
                                  to (utils/split-and-trim to #" ")
                                  to (set to)]
                              [from to]))]
    (into {} (map parse-row input))))

(defn fill-missing-edges
  [graph]
  (reduce (fn [g k] (reduce #(update %1 %2 (fn [s] (if (nil? s) #{k} (conj s k)))) g (get g k))) graph (keys graph)))

(defn find-random-edge
  [g]
  (let [edge-u (rand-nth (keys g))
        edge-v (-> (get g edge-u) vec rand-nth)]
    [edge-u edge-v]))

(defn karger
  [graph]
  (loop [g graph
         groups (reduce #(assoc %1 %2 #{%2}) {} (keys g))]
    (if (<= (count g) 2) (vals groups)
        (let [[edge-u edge-v] (find-random-edge g)
              new-g (update g edge-u #(set/union % (get g edge-v)))
              new-g (reduce (fn [acc n]
                              (let [remove-v (update acc n #(disj % edge-v))
                                    add-u (update remove-v n #(conj % edge-u))]
                                add-u)) new-g (get new-g edge-v))
              new-g (update new-g edge-u #(disj % edge-u))
              new-g (dissoc new-g edge-v)
              new-groups (update groups edge-u #(set/union % (get groups edge-v)))
              new-groups (dissoc new-groups edge-v)]
          (recur new-g new-groups)))))

(defn find-cuts
  [graph [group1 group2]]
  (->> (for [x group1
             y group2]
         [x y])
       (filter #(contains? (get graph (first %)) (second %)))))

(defn part-1 [input]
  (let [graph (parse input)
        graph (fill-missing-edges graph)]
    (loop [groups (karger graph)]
      (if (= 3 (count (find-cuts graph groups))) (reduce * (map count groups))
          (recur (karger graph))))))