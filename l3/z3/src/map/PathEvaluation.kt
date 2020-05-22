package map

import map.enums.FieldType
import solver.enums.Moves
import solver.enums.PathState

data class PathEvaluation(
    val fields: Map<Moves, FieldType> = mapOf(),
    val state: PathState,
    val length: Int
) {
    fun winningMove() = fields.entries.find { it.value == FieldType.EXIT }
}