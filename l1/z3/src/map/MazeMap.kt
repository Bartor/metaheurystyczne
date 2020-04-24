package map

import map.enums.FieldType
import tabu.Path
import tabu.enums.Moves
import tabu.enums.PathState

class MazeMap(private val rows: Int, private val columns: Int) {
    // space O(n*m) solution for now, to fix later
    private val layout: Array<Array<FieldType>> = Array(rows) { Array(columns) { FieldType.VALID } }
    private var agentStartRow = -1
    private var agentStartCol = -1

    private fun getFieldSafe(row: Int, col: Int): FieldType =
        layout.getOrElse(row) { emptyArray() }.getOrElse(col) { FieldType.INVALID }

    fun setRow(rowNumber: Int, representation: Array<Int>) {
        if (representation.contains(FieldType.AGENT.value)) {
            agentStartRow = rowNumber
            agentStartCol = representation.indexOf(FieldType.AGENT.value)
        }

        layout[rowNumber] =
            representation.map { FieldType.values().find { value -> value.value == it }!! }.toTypedArray()
    }

    fun evaluatePath(path: Path): PathEvaluation {
        var currentRow = agentStartRow
        var currentCol = agentStartCol

        for ((moves, move) in path.sequence.withIndex()) {
            when (move) {
                Moves.U -> currentRow--
                Moves.D -> currentRow++
                Moves.L -> currentCol--
                Moves.R -> currentCol++
            }
            when (getFieldSafe(currentRow, currentCol)) {
                FieldType.WALL, FieldType.INVALID -> return PathEvaluation(
                    state = PathState.BLOCKED,
                    length = moves + 1
                )
                FieldType.EXIT -> return PathEvaluation(state = PathState.WINNING, length = moves + 1)
            }
        }

        return PathEvaluation(
            mapOf(
                Moves.R to getFieldSafe(currentRow, currentCol + 1),
                Moves.L to getFieldSafe(currentRow, currentCol - 1),
                Moves.D to getFieldSafe(currentRow + 1, currentCol),
                Moves.U to getFieldSafe(currentRow - 1, currentCol)
            ),
            PathState.IN_PROGRESS,
            path.sequence.size
        )
    }
}