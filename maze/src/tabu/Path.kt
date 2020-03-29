package tabu

import map.MazeMap
import tabu.enums.Moves

class Path() {
    val sequence = mutableListOf<Moves>()

    override fun equals(other: Any?): Boolean {
        if (other !is Path) return false
        if (other.sequence.size != sequence.size) return false

        for (i in sequence.indices) {
            if (other.sequence[i] != sequence[i]) return false
        }

        return true
    }

    fun append(move: Moves): Path {
        sequence.add(move)
        return this
    }

    override fun toString(): String {
        return sequence.toString()
    }
}
