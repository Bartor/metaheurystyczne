package tabu

import kotlin.random.Random

fun <T> Array<T>.swap(a: Int, b: Int): List<T> = this.toMutableList().also {
    it[a] = this[b]
    it[b] = this[a]
}

class Path(val cities: Array<Int>, private val distances: Distances) {
    fun length(): Long {
        var sum = 0L
        for (i in 0 until cities.size - 1) {
            sum += distances.distance(cities[i], cities[i + 1])
        }
        sum += distances.distance(cities.last(), cities.first())
        return sum
    }

    fun randomTweak(): Path {
        val i = Random.nextInt(distances.citiesCount)
        val j = Random.nextInt(distances.citiesCount)

        return when (Random.nextInt(4)) {
            0, 1, 2, 3 -> transpose(i, j) // yes this case is exhaustive, but getting rid of inverses help
            else -> inverse(i, j)
        }
    }

    private fun transpose(i: Int, j: Int) = Path(cities.copyOf().swap(i, j).toTypedArray(), distances)

    private fun inverse(from: Int, to: Int): Path = Path(
        (cities.slice(0 until from) +
                cities.slice(from..to).reversed() +
                cities.slice(to + 1 until cities.size)).toTypedArray(),
        distances
    )

    override fun equals(other: Any?): Boolean {
        if (other !is Path) return false
        if (other.cities.size != cities.size) return false
        for (i in cities.indices) {
            if (other.cities[i] != cities[i]) return false
        }
        return true
    }

    override fun toString(): String {
        return "${cities.joinToString(" ")} ${cities.first()}"
    }
}