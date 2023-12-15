(ns advent-of-code.day-07
  (:require [clojure.string :as string]))

(defn parse-row
  [row]
  (let [[hand bid] (string/split row #" ")
        bid        (read-string bid)]
    {:hand hand
     :bid  bid}))

(defn get-strength
  [hand]
  (let [freq (frequencies hand)
        freq (frequencies (vals freq))]
    (cond (= (get freq 5) 1) 6
          (= (get freq 4) 1) 5
          (and (= (get freq 3) 1) (= (get freq 2) 1)) 4
          (= (get freq 3) 1) 3
          (= (get freq 2) 2) 2
          (= (get freq 2) 1) 1
          (= (count freq) 5) 0
          :else -1)))

(def cards "AKQJT98765432")

(def strength (->> cards
                   (#(string/split % #""))
                   reverse
                   (#(map vector % (range)))
                   (into (hash-map))))

(defn compare-cards
  [first second strength-map]
  (compare (apply vector (map #(get strength-map (str %)) first))
           (apply vector (map #(get strength-map (str %)) second))))


(defn hand-cmp
  [first second]
  (let [fs (get-strength (:hand first))
        ss (get-strength (:hand second))]
    (if-not (= fs ss) (- fs ss) (compare-cards (:hand first) (:hand second) strength))))

(defn part-1
  [input]
  (->> input
       (map parse-row)
       (sort hand-cmp)
       (map #(* (inc %1) (:bid %2)) (range))
       (reduce +)))


(def cards-without-j (filter #(not= % \J) cards))

(defn get-all-possible-cards
  [hand]
  (let [without-j (string/join #"" (filter #(not= % \J) hand))
        j-count  (get (frequencies hand) \J 0)]
    (reduce (fn [acc _]
              (for [h acc
                    c cards-without-j]
                (str h c))) [without-j] (range j-count))))

(defn get-strength-replace-j
  [hand]
  (->> hand
       get-all-possible-cards
       (map get-strength)
       (apply max)))

(def cards-part-2 "AKQT98765432J")

(def strength-part-2 (->> cards-part-2
                          (#(string/split % #""))
                          reverse
                          (#(map vector % (range)))
                          (into (hash-map))))

(defn hand-cmp-part-2
  [first second]
  (let [fs (get-strength-replace-j (:hand first))
        ss (get-strength-replace-j (:hand second))]
    (if-not (= fs ss) (- fs ss) (compare-cards (:hand first) (:hand second) strength-part-2))))

(defn part-2
  [input]
  (->> input
       (map parse-row)
       (sort hand-cmp-part-2)
       (map #(* (inc %1) (:bid %2)) (range))
       (reduce +)))