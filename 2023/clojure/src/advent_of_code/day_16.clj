(ns advent-of-code.day-16)

(defn parse
  [input]
  (map seq input))

(defn get-value
  [input [row col]]
  (-> input
      (nth row)
      (nth col)))

(defn in-bounds
  [input [row col]]
  (and (< -1 row (count input))
       (< -1 col (count (first input)))))

(defn next-points
  [input visited point dirs]
  (->> dirs
       (map #(list (map + point %) %))
       (filter #(in-bounds input (first %)))
       (filter #(not (contains? visited %)))))

(defn new-dirs
  [tile dir]
  (case tile
    \. [dir]
    \/ [(map - (reverse dir))]  ;; ([0 1] [-1 0], [1 0] [0 -1], [0 -1] [1 0], [-1 0] [0 1]) 
    \\ [(reverse dir)] ;; ([0 1] [1 0], [1 0] [0 1], [0 -1] [-1 0], [-1 0] [0 -1]) 
    \| (if (not= (second dir) 0) [[1 0] [-1 0]] [dir])
    \- (if (not= (first dir) 0) [[0 1] [0 -1]] [dir])))

(defn travel
  [input [start-point start-dir]]
  (loop [frontier [[start-point start-dir]]
         visited #{}]
    (if (empty? frontier) visited
        (let [[point dir] (first frontier)
              new-frontier (rest frontier)
              tile (get-value input point)
              new-dirs (new-dirs tile dir)
              next-points (next-points input visited point new-dirs)
              new-frontier (concat new-frontier next-points)]
          (recur new-frontier (conj visited [point dir]))))))

(defn energised-points
  [visited]
  (->> visited
       seq
       (map first)
       set
       count))

(defn part-1
  [input]
  (-> input
      parse
      (travel [[0 0] [0 1]])
      energised-points))

(defn part-2
  [input]
  (let [input (parse input)
        max-row (dec (count input))
        max-col (-> input first count dec)
        possible-starts (concat
                         (map #(list [% 0] [0 1]) (range (inc max-row)))
                         (map #(list [% max-col] [0 -1]) (range (inc max-row)))
                         (map #(list [0 %] [1 0]) (range (inc max-col)))
                         (map #(list [max-row %] [-1 0]) (range (inc max-col))))]
    (->> possible-starts
         (map (partial travel input))
         (map energised-points)
         (apply max))))