package solver

import solver.enums.Moves
import kotlin.random.Random

class Path(val sequence: MutableList<Moves> = mutableListOf()) {
    private fun <T> Array<T>.swap(a: Int, b: Int): Array<T> = this.also {
        it[a] = this[b]
        it[b] = this[a]
    }

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
        var i = Random.nextInt(sequence.size)
        var j = Random.nextInt(sequence.size)

        if (i > j) i = j.also { j = i }

        return when (Random.nextInt(4)) {
            0, 1 -> transpose(i, j)
            else -> inverse(i, j)
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
