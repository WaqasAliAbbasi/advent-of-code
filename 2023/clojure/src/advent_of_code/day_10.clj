(ns advent-of-code.day-10)

(def symbol-to-direction
  ;    N E S W
  {\| [1 0 1 0]
   \- [0 1 0 1]
   \L [1 1 0 0]
   \J [1 0 0 1]
   \7 [0 0 1 1]
   \F [0 1 1 0]
   \. [0 0 0 0]
   \S [1 1 1 1]})

(defn parse-row
  [row]
  (->> row
       (map symbol-to-direction)))

(defn parse
  [input]
  (->> input
       (map parse-row)))

(defn get-value
  [input x y]
  (when
   (and (< -1 x (count (first input)))
        (< -1 y (count input))) (nth (nth input y) x)))

(defn find-s
  [input]
  (let [input (for [y (range (count input))
                    x (range (count (first input)))]
                (when (= (get-value input x y) \S) [x y]))]
    (->> input
         (filter not-empty)
         first)))

(defn get-options
  [input x y]
  (let [[north east south west] (get-value input x y)
        right (get-value input (inc x) y)
        left (get-value input (dec x) y)
        up (get-value input x (dec y))
        down (get-value input x (inc y))]
    (->> [(and (= north 1) (= (nth up 2) 1) [x (dec y)])
          (and (= east 1) (= (nth right 3) 1) [(inc x) y])
          (and (= south 1) (= (nth down 0) 1) [x (inc y)])
          (and (= west 1) (= (nth left 1) 1) [(dec x) y])]
         (filter #(not (boolean? %)))
         set)))

(defn find-loop
  [input start]
  ((fn dfs [explored frontier]
     (lazy-seq (if (empty? frontier) nil
                   (let [[x y] (peek frontier)
                         neighbours (get-options input x y)]
                     (cons [x y] (dfs (into explored neighbours) (into (pop frontier) (remove explored neighbours)))))))) #{start} [start]))


(defn can-reach-outside
  [raw main-loop [x y]]
  (->> (range 0 x)
       (filter #(contains? main-loop [% y]))
       (map #(get-value raw % y))
       (apply str)
       (re-seq #"L\-*7|F\-*J|\|")
       count
       odd?))

(defn part-1
  [input]
  (let [parsed (parse input)
        s      (find-s input)]
    (-> (find-loop parsed s)
        count
        (/ 2))))

(defn part-2
  [input]
  (let [parsed (parse input)
        s      (find-s input)
        main-loop   (find-loop parsed s)
        main-loop   (set main-loop)
        can-reach   (for [y (range (count input))
                          x (range (count (first input)))]
                      (when (not (contains? main-loop [x y])) (can-reach-outside input main-loop [x y])))]
    (->> can-reach
         (filter #(not (nil? %)))
         (filter true?)
         count)))