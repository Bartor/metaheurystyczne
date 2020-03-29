package tabu

class Distances(size: Int) {
    private val relations: Array<Array<Int>> = Array(size) { Array(size) { 0 } }

    val citiesCount = relations.size

    fun setCity(city: Int, distances: Array<Int>) {
        relations[city] = distances
    }

    fun distance(from: Int, to: Int): Int {
        return relations[from][to]
    }

    fun naiveSolution(from: Int = 0): Path {
        val path = mutableListOf<Int>(from)
        var current = from
        while (path.size != relations.size) {
            var minDistance = Int.MAX_VALUE
            var minIndex = Int.MAX_VALUE

            for (i in relations[current].indices) {
                if (relations[current][i] < minDistance && !path.contains(i)) {
                    minDistance = relations[current][i]
                    minIndex = i
                }
            }

            path.add(minIndex)
            current = minIndex
        }

        return Path(path.toTypedArray(), this)
    }
}