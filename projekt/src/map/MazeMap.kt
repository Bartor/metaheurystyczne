package map

import map.enums.FieldType
import solver.Path
import solver.enums.Moves

class MazeMap(val rows: Int, val columns: Int) {
    private val layout: Array<Array<FieldType>> = Array(rows) { Array(columns) { FieldType.VALID } }
    private var agentStartRow = -1
    private var agentStartCol = -1

    private fun getFieldSafe(row: Int, col: Int): FieldType =
        layout.getOrElse(row) { emptyArray() }.getOrElse(col) { FieldType.INVALID }

    private fun getFieldNeighbours(row: Int, col: Int): Map<FieldType, Moves> =
        mapOf(
            getFieldSafe(row + 1, col) to Moves.D, getFieldSafe(row - 1, col) to Moves.U,
            getFieldSafe(row, col + 1) to Moves.R, getFieldSafe(row, col - 1) to Moves.L
        )

    fun setRow(rowNumber: Int, representation: Array<Int>) {
        if (representation.contains(FieldType.AGENT.value)) {
            agentStartRow = rowNumber
            agentStartCol = representation.indexOf(FieldType.AGENT.value)
        }

        layout[rowNumber] =
            representation.map { FieldType.values().find { value -> value.value == it }!! }.toTypedArray()
    }

    fun evaluatePath(path: Path): Path {
        var currentRow = agentStartRow
        var currentCol = agentStartCol
        var previousFieldType = FieldType.AGENT

        for ((moves, move) in path.sequence.withIndex()) {
            when (move) {
                Moves.U -> currentRow--
                Moves.D -> currentRow++
                Moves.L -> currentCol--
                Moves.R -> currentCol++
            }

            val currentField = getFieldSafe(currentRow, currentCol)
            when (currentField) {
                FieldType.TUNNEL_LR -> if (move != Moves.R && move != Moves.L) return Path()
                FieldType.TUNNEL_UD -> if (move != Moves.U && move != Moves.D) return Path()
                FieldType.WALL, FieldType.INVALID -> return Path()
                FieldType.VALID, FieldType.EXIT -> {
                    if (previousFieldType == FieldType.TUNNEL_UD && move != Moves.U && move != Moves.D) return Path()
                    if (previousFieldType == FieldType.TUNNEL_LR && move != Moves.R && move != Moves.L) return Path()
                    if (currentField == FieldType.EXIT) return Path(path.sequence.subList(0, moves + 1))
                }
            }

            // check if you can just jump from current field into neighboring exit
            getFieldNeighbours(currentRow, currentCol)[FieldType.EXIT].also {
                if (it != null) {
                    if ((currentField == FieldType.TUNNEL_LR && (it == Moves.L || it == Moves.R)) || // move LR in T_LR
                        (currentField == FieldType.TUNNEL_UD && (it == Moves.D || it == Moves.U)) || // move UD in T_UD
                        (currentField != FieldType.TUNNEL_UD && currentField != FieldType.TUNNEL_LR) // move any way
                    ) {
                        return Path(path.sequence.subList(0, moves + 1) + it)
                    }
                }
            }

            previousFieldType = currentField
        }

        return Path()
    }
}