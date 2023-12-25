(ns advent-of-code.day-23)

(defn parse
  [input]
  (map seq input))

(defn get-value
  [graph [row col]]
  (nth (nth graph row) col))

(defn in-bounds
  [graph [row col]]
  (and (< -1 row (count graph))
       (< -1 col (count (first graph)))))

(defn get-dirs
  [graph current]
  (case (get-value graph current)
    \. [[0 1] [0 -1] [1 0] [-1 0]]
    \> [[0 1]]
    \< [[0 -1]]
    \^ [[-1 0]]
    \v [[1 0]]))

(defn next-moves-part-1
  [graph current]
  (->> (get-dirs graph current)
       (map (fn [dir] (map + dir current)))
       (filter #(in-bounds graph %))
       (filter #(not= \# (get-value graph %)))
       (map #(list % 1))))

(defn search
  [adj start end]
  (loop [frontier (vector [#{} start 0])
         best     0]
    (if (empty? frontier) best
        (let [[visited current current-best] (last frontier)
              neighbors        (get adj current [])
              neighbors        (filter #(not (contains? visited (first %))) neighbors)
              neighbors        (map #(list (conj visited current) (first %) (+ current-best (second %))) neighbors)
              new-frontier     (concat (drop-last frontier) neighbors)
              new-best         (if (= end current) (max best current-best) best)]
          (recur new-frontier new-best)))))

(defn replace-with-current
  [target current new-destination add-d]
  (let [target-without-current (filter #(not= current (first %)) target)
        target-with-current (first (filter #(= current (first %)) target))]
    (if (nil? target-with-current) target-without-current (conj target-without-current [new-destination (+ (second target-with-current) add-d)]))))

(defn prune-matrix
  [adj]
  (loop [q (filter #(= 2 (count (get adj %))) (keys adj))
         a adj]
    (if (empty? q) a
        (let [current (first q)
              [[left left-d] [right right-d]] (get a current)
              new-a (update a left #(replace-with-current % current right right-d))
              new-a (update new-a right #(replace-with-current % current left left-d))
              new-a (dissoc new-a current)
              new-queue (rest q)]
          (recur new-queue new-a)))))

(defn adj-matrix
  [graph next-moves-fn]
  (let [positions (for [row (-> graph count range)
                        col (-> graph first count range)]
                    [row col])
        helper (fn [adj p]
                 (if (or (not (in-bounds graph p)) (= \# (get-value graph p))) adj (assoc adj p (next-moves-fn graph p))))
        adj (reduce helper {} positions)]
    (prune-matrix adj)))

(defn part-1 [input]
  (let [graph (parse input)
        adj (adj-matrix graph next-moves-part-1)
        start [0 (first (filter #(= \. (get-value graph [0 %])) (range)))]
        end   [(-> graph count dec) (first (filter #(= \. (get-value graph [(-> graph count dec) %])) (range)))]]
    (search adj start end)))

(defn next-moves-part-2
  [graph current]
  (->> [[0 1] [0 -1] [1 0] [-1 0]]
       (map (fn [dir] (map + dir current)))
       (filter #(in-bounds graph %))
       (filter #(not= \# (get-value graph %)))
       (map #(list % 1))))

(defn part-2 [input]
  (let [graph (parse input)
        adj (adj-matrix graph next-moves-part-2)
        start [0 (first (filter #(= \. (get-value graph [0 %])) (range)))]
        end   [(-> graph count dec) (first (filter #(= \. (get-value graph [(-> graph count dec) %])) (range)))]]
    (search adj start end)))