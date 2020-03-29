package tabu

import tabu.enums.Moves
import kotlin.random.Random

fun <T> Array<T>.swap(a: Int, b: Int): Array<T> = this.also {
    it[a] = this[b]
    it[b] = this[a]
}

class Path(val sequence: MutableList<Moves> = mutableListOf()) {
    fun append(move: Moves): Path {
        sequence.add(move)
        return this
    }

    private fun transpose(i: Int, j: Int): Path = Path(mutableListOf(*sequence.toTypedArray().swap(i, j)))


    private fun inverse(from: Int, to: Int): Path = Path(
        (sequence.slice(0 until from) +
                sequence.slice(from..to).reversed() +
                sequence.slice(to + 1 until sequence.size))
            .toMutableList()
    )


    fun randomTweak(): Path {
        val i = Random.nextInt(sequence.size)
        val j = Random.nextInt(sequence.size)

        return when (Random.nextInt(4)) {
            0, 1 -> transpose(i, j)
            else -> inverse(j, i)
        }
    }

    override fun toString(): String {
        return sequence.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Path) return false
        if (other.sequence.size != sequence.size) return false

        for (i in sequence.indices) {
            if (other.sequence[i] != sequence[i]) return false
        }

        return true
    }
}
