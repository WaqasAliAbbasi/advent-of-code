(ns advent-of-code.day-24
  (:require [clojure.string :as str]
            [advent-of-code.utils :as utils]
            [clojure.math.combinatorics :as combo]
            [clojure.set :as set]))

(defn parse
  [input]
  (let [parse-row (fn [row]
                    (let [[position velocity] (str/split row #"@")
                          position (utils/parse-numbers position #",")
                          velocity (utils/parse-numbers velocity #",")] [position velocity]))]
    (map parse-row input)))

(defn find-line
  [[[px py _] [vx vy _]]]
  (let [m (/ vy vx)
        c (- py (* px m))]
    [m c]))

(defn find-intersection
  [[a b]]
  (let [[m1 c1] (find-line a)
        [m2 c2] (find-line b)
        top (- c2 c1)
        bottom (- m1 m2)
        x (if (= 0 bottom) nil (/ top bottom))
        y (if (nil? x) nil (+ (* m1 x) c1))]
    [x y [a b]]))

(defn within-range
  [min max value]
  (<= min value max))

(defn in-future?
  [x y [[px py _] [vx vy]]]
  (and (pos? (/ (- x px) vx))
       (pos? (/ (- y py) vy))))

(defn in-future-intersection?
  [[x y [a b]]]
  (and (in-future? x y a) (in-future? x y b)))

(defn part-1 [input]
  (let [min-range 200000000000000
        max-range 400000000000000
        parsed (parse input)
        comb (combo/combinations parsed 2)
        intersections (->> comb
                           (map find-intersection)
                           (filter #(not (nil? (first %))))
                           (filter #(and (within-range min-range max-range (first %)) (within-range min-range max-range (second %)))))]
    (-> (filter in-future-intersection? intersections)
        count)))

(defn find-new
  [old-set a b idx]
  (if (not= (-> a second (nth idx)) (-> b second (nth idx))) old-set
      (let [difference (- (-> b first (nth idx)) (-> a first (nth idx)))

            potential  (filter #(not= % (-> a second (nth idx))) (range -1000 1000))
            potential  (filter #(= 0 (mod difference (- % (-> a second (nth idx))))) potential)
            new-set (set potential)]
        (if (empty? old-set) new-set (set/intersection old-set new-set)))))

(defn find-potential
  [combinations]
  (let [helper (fn [[potential-x potential-y potential-z] [a b]] (let [new-x (find-new potential-x a b 0)
                                                                       new-y (find-new potential-y a b 1)
                                                                       new-z (find-new potential-z a b 2)]
                                                                   [new-x new-y new-z]))]
    (reduce helper [#{} #{} #{}] combinations)))

(defn find-rock
  [[rvx rvy rvz] [[apx apy apz] [avx avy avz]] [[bpx bpy _] [bvx bvy _]]]
  (let [ma (/ (- avy rvy) (- avx rvx))
        mb (/ (- bvy rvy) (- bvx rvx))
        ca (- apy (* ma apx))
        cb (- bpy (* mb bpx))
        rpx (/ (- cb ca) (- ma mb))
        rpy (+ ca (* ma rpx))
        t (/ (- rpx apx) (- avx rvx))
        rpz (+ apz (* t (- avz rvz)))]
    [rpx rpy rpz]))

(defn part-2 [input]
  (let [parsed (parse input)
        comb (combo/combinations parsed 2)
        common (find-potential comb)
        [rvx rvy rvz] (map #(-> common (nth %) first) (range 3))]
    (->> (find-rock [rvx rvy rvz] (first parsed) (second parsed))
         (reduce +))))