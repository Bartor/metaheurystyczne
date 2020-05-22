package solver

import solver.enums.Moves
import kotlin.random.Random

class Path(val sequence: MutableList<Moves> = mutableListOf()) {
    private fun <T> Array<T>.swap(a: Int, b: Int): Array<T> = this.also {
        it[a] = this[b]
        it[b] = this[a]
    }

    fun append(vararg moves: Moves): Path {
        sequence.addAll(moves)
        return this
    }

    public fun crossover(other: Path): List<Path> {
        val (larger, smaller) = listOf(this, other).sortedByDescending { it.sequence.size }
        val indices = larger.sequence.map { Random.nextDouble() < 1 / larger.sequence.size }
        return listOf(
            Path(larger.sequence.mapIndexed { i, m -> if (indices[i] || smaller.sequence.size <= i) m else smaller.sequence[i] }
                .toMutableList()),
            Path(smaller.sequence.mapIndexed { i, m -> if (!indices[i]) m else smaller.sequence[i] }
                .toMutableList())
        )
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
