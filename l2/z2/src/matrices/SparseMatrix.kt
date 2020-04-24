package matrices

import java.lang.Exception
import kotlin.random.Random

class SparseMatrix(
    private val width: Int,
    private val height: Int
) : CoolMatrix {
    private val representation: MutableList<SparseBlock> = mutableListOf()

    public fun addBlocks(vararg blocks: SparseBlock) {
        this.representation.addAll(blocks)
    }

    // this is basically a one-liner which creates new SparseMatrix
    // with a random block with value randomly changed
    // god forgive me
    fun randomizeBlockValueTweak() = SparseMatrix(width, height).apply {
        addBlocks(*representation.apply {
            Random.nextInt(representation.size).let {
                set(it, get(it).copy().apply { value = SparseValues.available.random() })
            }
        }.toTypedArray())
    }

    override fun distance(other: CoolMatrix): Double {
        var sum = 0.0
        for (block in representation) with(block) {
            for (r in row until row + height) for (c in column until column + width) {
                // kotlin may not have integer powers, but it has scope functions
                sum += (value - other.getValue(r, c)).let { it * it }
            }
        }

        return sum / (width * height)
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