(ns advent-of-code.day-15
  (:require [clojure.string :as string]))

(defn get-hash
  [row]
  (reduce (fn [current c] (-> current
                              (+ (int c))
                              (* 17)
                              (rem 256))) 0 row))

(defn parse-steps
  [input]
  (-> input
      first
      (string/split #",")))

(defn part-1
  [input]
  (->> input
       parse-steps
       (map get-hash)
       (reduce +)))

(defn parse-remove
  [step]
  (let [label (subs step 0 (dec (count step)))]
    {:step :remove
     :label label
     :box   (get-hash label)}))

(defn parse-assign
  [step]
  (let [[label focal] (string/split step #"=")
        lens (read-string focal)]
    {:step :add
     :label label
     :box   (get-hash label)
     :lens  lens}))

(defn parse-instruction
  [step]
  (if (= \- (last step)) (parse-remove step) (parse-assign step)))

(defmulti execute #(:step %2))

(defmethod execute :add
  [state {:keys [box label lens]}]
  (update state box
          #(update % label (fn [[old-time _]] [(or old-time (System/nanoTime)) lens]))))

(defmethod execute :remove
  [state {:keys [box label]}]
  (update state box #(dissoc % label)))

(defn focusing-power
  [lens box]
  (->> lens
       vals
       sort
       (map second)
       (map #(* box (inc %1) %2) (range))
       (reduce +)))

(defn focusing-power-all-boxes
  [state]
  (map #(focusing-power (get state %) (inc %)) (keys state)))

(defn part-2
  [input]
  (->> input
       parse-steps
       (map parse-instruction)
       (reduce execute {})
       focusing-power-all-boxes
       (reduce +)))