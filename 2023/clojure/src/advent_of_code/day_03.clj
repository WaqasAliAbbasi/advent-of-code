(ns advent-of-code.day-03)

(defn is-number
  [character]
  (and (>= (int character) (int \0))
       (<= (int character) (int \9))))

(defn is-symbol
  [character]
  (and
   (not= character \.)
   (not (is-number character))))

(defn get-symbol-positions
  [input]
  (->> input
       (filter #(is-symbol (first %)))
       (map second)))

(defn get-adjacent-position
  [[x y]]
  [[(- x 1) (+ y 1)]
   [(- x 1) y]
   [(- x 1) (- y 1)]
   [x (- y 1)]
   [x (+ y 1)]
   [(+ x 1) (+ y 1)]
   [(+ x 1) y]
   [(+ x 1) (- y 1)]])

(defn get-row-chars-with-position
  [row row-number]
  (map #(vector %1 (vector row-number %2)) row (range)))

(defn get-input-with-positons
  [input]
  (map get-row-chars-with-position input (range)))

(defn vectorize
  [xs]
  (into [] xs))

(defn filter-parts
  [row]
  (reduce (fn [acc x]
            (if (is-number (first x)) (conj (vectorize (drop-last acc)) (conj (vectorize (last acc)) x))
                (conj acc []))) [] row))

(defn get-parts
  [input-with-positons]
  (->> (mapcat filter-parts input-with-positons)
       (filter seq)))

(defn part-adjacent-to-symbol
  [part adjacent-positions]
  (some #(contains? adjacent-positions (second %)) part))

(defn get-part-value
  [part]
  (read-string (apply str (map first part))))

(defn part-1
  [input]
  (let [input-with-positons (get-input-with-positons input)
        symbol-positions (mapcat get-symbol-positions input-with-positons)
        adjacent-positions (set (mapcat get-adjacent-position symbol-positions))]
    (->> (get-parts input-with-positons)
         (filter #(part-adjacent-to-symbol % adjacent-positions))
         (map get-part-value)
         (reduce +))))

(defn map-position-to-part-value
  [parts]
  (into (hash-map) (mapcat (fn [part] (map #(vector (second %) (get-part-value part)) part)) parts)))

(defn get-adjacent-parts
  [position position-to-part-id]
  (->> position
       get-adjacent-position
       (map #(get position-to-part-id %))
       set
       seq
       (filter #(not (nil? %)))))

(defn part-2
  [input]
  (let [input-with-positons (get-input-with-positons input)
        parts (get-parts input-with-positons)
        position-to-part-id (map-position-to-part-value parts)]
    (->> input-with-positons
         (mapcat identity)
         (filter #(= (first %) \*))
         (map #(get-adjacent-parts (second %) position-to-part-id))
         (filter #(= 2 (count %)))
         (map #(reduce * %))
         (reduce +))))