(ns advent-of-code.day-20
  (:require [clojure.string :as string]
            [advent-of-code.utils :as utils]
            [clojure.math.numeric-tower :as math]))

(defn parse-row
  [row]
  (let [[name to] (string/split row #" -> ")
        to (utils/split-and-trim to #",")
        type (case (first name)
               \b :broadcaster
               \& :conjunction
               \% :flip-flop)
        name (if (= name "broadcaster") "broadcaster" (subs name 1 (count name)))
        state {}
        state (if (= type :flip-flop) (assoc state :on false) state)]
    [name {:inputs []
           :name name
           :type type
           :to   to
           :state state}]))

(defn parse
  [input]
  (apply hash-map (mapcat parse-row input)))

(defn calculate-inputs
  [modules]
  (let [update-modules (fn [modules from destinations]
                         (reduce (fn [acc d] (update-in acc [d :inputs] #(conj % from))) modules destinations))]
    (reduce #(update-modules %1 (:name %2) (:to %2)) modules (vals modules))))

(defn make-steps
  [from pulse destinations]
  (map #(list from pulse %) destinations))

(defn get-next-step
  [modules new-frontier [from pulse destination-name]]
  (let [destination (get modules destination-name)]
    (case (:type destination)
      :broadcaster [(concat new-frontier (make-steps destination-name pulse (:to destination))) modules]
      :flip-flop   (if (= pulse :high) [new-frontier modules] [(concat new-frontier
                                                                       (make-steps destination-name
                                                                                   (if (get-in modules [destination-name :state :on]) :low :high)
                                                                                   (:to destination)))
                                                               (update-in modules [destination-name :state :on] not)])
      :conjunction (let [new-modules (update-in modules [destination-name :state from] (constantly pulse))
                         input-pulses (map #(get-in new-modules [destination-name :state %] :low) (:inputs destination))
                         all-high    (every? #(= :high %) input-pulses)
                         new-pulse   (if all-high :low :high)]
                     [(concat new-frontier (make-steps destination-name new-pulse (:to destination))) new-modules])
      [new-frontier modules])))

(defn transmit-once
  [[m r] target]
  (loop [frontier [["button" :low "broadcaster"]]
         modules  m
         result   r]
    (if (or (empty? frontier) (:found result)) [modules result]
        (let [current (first frontier)
              new-frontier (rest frontier)
              [new-frontier new-modules] (get-next-step modules new-frontier current)
              result (update result (second current) inc)
              result (if (= target current) (assoc result :found true) result)]
          (recur new-frontier new-modules result)))))

(defn part-1
  [input]
  (let [modules (parse input)
        modules (calculate-inputs modules)
        [_ result] (reduce (fn [m _] (transmit-once m nil)) [modules {:high 0 :low 0}] (range 1000))]
    (* (:high result) (:low result))))

(defn required-cycles
  [modules target]
  (loop [m [modules {:high 0 :low 0}]
         attempts 0]
    (if (true? (-> m second :found)) attempts
        (recur (transmit-once m target) (inc attempts)))))

(defn part-2
  [input]
  (let [modules (parse input)
        modules (calculate-inputs modules)
        target  "rx"
        before-target (->> (get modules target) :inputs first)
        required-high (-> (get modules before-target) :inputs)
        required-high (map #(required-cycles modules [% :high before-target]) required-high)]
    (->> required-high
         (reduce math/lcm))))