(ns advent-of-code.day-14
  (:require [advent-of-code.utils :as utils]))

(defn get-position
  [row]
  (let [row-with-index (map vector row (range))]
    (reduce (fn [[xs hashes last] [c index]]
              [(cond (= c \O) (conj xs last)
                     (= c \#) xs
                     (= c \.) xs)
               (cond (= c \O) hashes
                     (= c \#) (conj hashes index)
                     (= c \.) hashes)
               (cond (= c \#) (inc index)
                     (= c \.) last
                     (= c \O) (inc last))])
            [[] [] 0] row-with-index)))

(defn transform-back
  [total [positions hashes _]]
  (let [blank (into (vector) (repeat total \.))
        blocks (reduce #(update %1 %2 (constantly \O)) blank positions)
        hashes (reduce #(update %1 %2 (constantly \#)) blocks hashes)]
    hashes))

(defn roll-north
  [input]
  (->> input
       utils/transpose
       (map get-position)
       (map (partial transform-back (count input)))
       utils/transpose))

(defn roll-west
  [input]
  (->> input
       (map get-position)
       (map (partial transform-back (count input)))))

(defn roll-east
  [input]
  (->> input
       (map reverse)
       (map get-position)
       (map (partial transform-back (count input)))
       (map reverse)))

(defn roll-south
  [input]
  (->> input
       utils/transpose
       (map reverse)
       (map get-position)
       (map (partial transform-back (count input)))
       (map reverse)
       utils/transpose))

(defn do-cycle
  [input]
  (-> input
      roll-north
      roll-west
      roll-south
      roll-east))

(def do-cycle-memo
  (memoize do-cycle))

(defn cycle-n
  [input n]
  (loop [state input
         hash (hash-map)
         acc 0]
    (if (= acc n)
      state
      (let [last-acc    (get hash state)
            loop-length (when-not (nil? last-acc) (- acc last-acc))
            remaining   (- n acc)
            new-acc     (if (nil? last-acc) acc (+ acc (* loop-length (quot remaining loop-length))))]
        (recur (do-cycle-memo state)
               (if (nil? last-acc) (assoc hash state acc) hash)
               (inc new-acc))))))

(defn get-power
  [n row]
  (* n (get (frequencies row) \O 0)))

(defn part-1
  [input]
  (->> input
       (map seq)
       roll-north
       (map get-power (range (count input) -1 -1))
       (reduce +)))

(defn part-2
  [input]
  (let [input (map seq input)
        after (cycle-n input 1000000000)]
    (->> after
         (map get-power (range (count after) -1 -1))
         (reduce +))))