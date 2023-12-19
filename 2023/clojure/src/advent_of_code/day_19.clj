(ns advent-of-code.day-19
  (:require [clojure.string :as string]))

(defn parse-part
  [row]
  (let [row (subs row 1 (-> row count dec))
        elements (string/split row #",")
        parse-element (fn [element] (let [[label value] (string/split element #"=")
                                          value (read-string value)]
                                      [label value]))
        elements (mapcat parse-element elements)]
    (apply hash-map elements)))

(defn find-target-helper
  [workflows part state]
  (let [workflow (get workflows state)
        [f & _] (keep #(% part) workflow)]
    f))

(defn find-target
  [workflows part]
  (loop [state "in"]
    (if (or (= state "R") (= state "A")) state (recur (find-target-helper workflows part state)))))

(defn parse
  [input parse-workflow-fn]
  (let [[workflows _ parts] (partition-by empty? input)
        workflows (mapcat parse-workflow-fn workflows)
        workflows (apply hash-map workflows)
        parts (map parse-part parts)]
    [workflows parts]))

(defn parse-workflow-part1
  [row]
  (let [[name rules] (string/split row #"\{")
        rules        (subs rules 0 (-> rules count dec))
        rules        (string/split rules #",")
        default      (last rules)
        rules        (drop-last rules)
        parse-rule   (fn [rule] (let [[condition target] (string/split rule #":")
                                      [element [op] value] (partition-by #(or (= % \<) (= % \>)) condition)
                                      element (string/join "" element)
                                      value (read-string (string/join "" value))
                                      op (if (= op \<) < >)]
                                  #(when (true? (some-> (get % element) (op value))) target)))
        rules        (map parse-rule rules)
        rules        (concat rules [(constantly default)])]
    [name rules]))

(defn part-1
  [input]
  (let [[workflows parts] (parse input parse-workflow-part1)]
    (->> parts
         (filter #(= "A" (find-target workflows %)))
         (map #(reduce + (vals %)))
         (reduce +))))

(defn parse-workflow-part2
  [row]
  (let [[name rules] (string/split row #"\{")
        rules        (subs rules 0 (-> rules count dec))
        rules        (string/split rules #",")
        default      (last rules)
        rules        (drop-last rules)
        parse-rule   (fn [rule] (let [[condition target] (string/split rule #":")
                                      [element [op] value] (partition-by #(or (= % \<) (= % \>)) condition)
                                      element (string/join "" element)
                                      value (read-string (string/join "" value))]
                                  [element op value target]))
        rules        (map parse-rule rules)]
    [name [rules default]]))

(defn get-new-state
  [current [el op value target]]
  (if (= op \<) [target (update current el #(list (first %) (min (second %) (dec value))))]
      [target (update current el #(list (max (first %) (inc value)) (second %)))]))

(defn get-complement-state
  [current [el op value _]]
  (if (= op \<) (update current el #(list (max (first %) value) (second %)))
      (update current el #(list (first %) (min (second %) value)))))

(defn get-new-frontier
  [frontier workflows state r]

  (let [[rules default] (get workflows state)
        [state-left new-states] (reduce (fn [[current acc] rule]
                                          [(get-complement-state current rule) (conj acc (get-new-state current rule))])
                                        [r []] rules)
        new-states (conj new-states [default state-left])]
    (concat frontier new-states)))

(defn traverse
  [workflows]
  (loop [frontier [["in" (hash-map "x" [1 4000] "m" [1 4000] "a" [1 4000] "s" [1 4000])]]
         result []]
    (if (empty? frontier) result
        (let [[state r] (first frontier)
              new-frontier (rest frontier)
              new-result (if (= "A" state) (conj result r) result)
              new-frontier (if (or (= "A" state) (= "R" state)) new-frontier (get-new-frontier new-frontier workflows state r))]
          (recur new-frontier new-result)))))

(defn part-2
  [input]
  (let [[workflows _] (parse input parse-workflow-part2)]
    (->> (traverse workflows)
         (map vals)
         (map (fn [comb] (map #(inc (- (second %) (first %))) comb)))
         (map (fn [comb] (reduce * comb)))
         (reduce +))))