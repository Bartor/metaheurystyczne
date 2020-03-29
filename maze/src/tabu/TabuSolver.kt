package tabu

import map.MazeMap
import map.enums.FieldType
import tabu.enums.Moves

class TabuSolver(private val map: MazeMap) {
    fun naiveSolution(): Path {
        val path = Path()

        var direction = Moves.values().random()
        var evaluation = map.evaluatePath(path)
        while (evaluation.winningMove() == null) {
            evaluation = map.evaluatePath(path)

            if (evaluation.fields[direction] == FieldType.WALL) {
                direction = Moves.values().find { it.value == (direction.value + 1).rem(4) }!!
                continue
            }

            path.append(direction)
        }

        return path.append(evaluation.winningMove()!!.key)
    }
}