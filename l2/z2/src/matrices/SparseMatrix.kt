package matrices

import java.lang.Exception
import kotlin.math.pow

class SparseMatrix(
    private val width: Int,
    private val height: Int
) : CoolMatrix {
    private val representation: List<SparseBlock> = mutableListOf()

    override fun distance(other: CoolMatrix): Double {
        var sum = 0.0
        for (block in representation) with(block) {
            for (r in row until row + height) for (c in column until column + width) {
                // kotlin may not have integer powers, but it has scope functions
                sum += (value - other.getValue(r, c)).let { it * it }
            }
        }

        return 1.0 / (width * height) * sum
    }

    override fun getValue(row: Int, column: Int): Short {
        for (block in representation) {
            if (
                row >= block.row &&
                row < block.row + block.height &&
                column >= block.column &&
                column < block.column + block.width
            ) {
                return block.value
            }
        }
        throw Exception("Matrix doesn't contain $row $column")
    }
}