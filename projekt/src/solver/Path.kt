package solver

import solver.enums.Moves
import kotlin.random.Random

class Path(val sequence: List<Moves> = emptyList()) {
    private fun <T> Array<T>.swap(a: Int, b: Int): Array<T> = this.also {
        it[a] = this[b]
        it[b] = this[a]
    }

    // swap random path elements
    private fun transpose(i: Int, j: Int): Path = Path(mutableListOf(*sequence.toTypedArray().swap(i, j)))

    // invert subsequence
    private fun inverse(from: Int, to: Int): Path = Path(
        sequence.slice(0 until from) +
                sequence.slice(from..to).reversed() +
                sequence.slice(to + 1 until sequence.size)
    )

    // add random moves at given index
    private fun extend(at: Int, times: Int): Path = Path(
        sequence.slice(0 until at) +
                arrayOfNulls<Moves>(times).map { Moves.values().random() } +
                sequence.slice(at until sequence.size)
    )

    private fun repeat(at: Int, times: Int): Path = Path(
        sequence.slice(0 until at) +
                Array(times) { sequence[at] } +
                sequence.slice(at until sequence.size)
    )

    private fun insertMove(at: Int, times: Int): Path = Moves.values().random().run {
        Path(
            sequence.slice(0 until at) +
                    Array(times) { this } +
                    sequence.slice(at until sequence.size)
        )
    }

    fun randomTweak(): Path {
        var i = Random.nextInt(sequence.size)
        var j = Random.nextInt(sequence.size)

        if (i > j) i = j.also { j = i }

        return when (Random.nextInt(3)) {
            0 -> transpose(i, j)
            1 -> inverse(i, j)
//            2 -> repeat(j, (j / 10.0).toInt())
//            3 -> insertMove(j, (j / 10.0).toInt())
            else -> extend(j, (j / 5.0).toInt())
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
