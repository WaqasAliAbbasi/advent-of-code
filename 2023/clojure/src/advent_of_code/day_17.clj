(ns advent-of-code.day-17
  (:require [ubergraph.alg :as alg]))

(defn parse
  [input]
  (map #(map (comp read-string str) %) input))

(defn in-bounds
  [input [row col]]
  (and (some? row)
       (some? col)
       (< -1 row (count input))
       (< -1 col (count (first input)))))

(defn get-value
  [input [row col]]
  (when (in-bounds input [row col])
    (-> input
        (nth row)
        (nth col))))

(def right [0 1])
(def up [-1 0])
(def down [1 0])
(def left [0 -1])

(def rotation-right
  {up right
   right down
   down left
   left up})

(def rotation-left
  {up left
   right up
   down right
   left down})

(defn rotate
  [dir turn-side]
  (case turn-side
    :R (get rotation-right dir)
    :L (get rotation-left dir)))

(defn do-turn [[p dir _] turn-side]
  (let [new-dir (rotate dir turn-side)]
    [(map + p new-dir) new-dir 1]))

(defn next-moves
  [graph possibles-fn [point dir moves]]
  (keep
   #(when-let [cost (and % (get-value graph (first %)))]
      {:dest % :weight cost})
   (possibles-fn [point dir moves])))

(defn travel
  [graph possibles-fn]
  (alg/shortest-path
   (partial next-moves graph possibles-fn)
   {:cost-attr :weight
    :start-nodes [[[0 0] [0 1] 0]
                  [[0 0] [1 0] 0]]
    :end-node?   #(= (first %) [(-> graph count dec)
                                (-> graph first count dec)])}))

(defn possibles-part1
  [[p dir moves :as state]]
  [(do-turn state :R)
   (do-turn state :L)
   (when (< moves 3)
     [(map + p dir) dir (inc moves)])])

(defn part-1
  [input]
  (-> input
      parse
      (travel possibles-part1)
      :cost))

(defn possibles-part2
  [[p dir moves :as state]]
  [(when (> moves 3) (do-turn state :R))
   (when (> moves 3) (do-turn state :L))
   (when (< moves 10)
     [(map + p dir) dir (inc moves)])])

(defn part-2
  [input]
  (-> input
      parse
      (travel possibles-part2)
      :cost))
