package matrices

import java.lang.Exception
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class SparseMatrix(
    private val width: Int,
    private val height: Int
) : CoolMatrix {
     val representation: MutableList<SparseBlock> = mutableListOf()

    public fun addBlocks(vararg blocks: SparseBlock) {
        this.representation.addAll(blocks)
    }

    public fun tweak(blockSize: Int): SparseMatrix {
        return when (Random.nextInt(2)) {
            0 -> randomizeBlockValueTweak()
            else -> swapBlockValuesTweaks()
            // splitting and merging either don't work or make the results worse so
//            2 -> mergeRandomBlocks() ?: randomizeBlockValueTweak()
//            else -> splitBigBlock(blockSize) ?: swapBlockValuesTweaks()
        }
    }

    private fun randomizeBlockValueTweak() = SparseMatrix(width, height)
        .also { it.addBlocks(*representation.map { it.copy() }.toTypedArray()) }
        .also {
            Random.nextInt(it.representation.size)
                .apply {
                    it.representation[this] = it.representation[this]
                        .apply { value = SparseValues.available.random() }
                }
        }

    private fun swapBlockValuesTweaks() = SparseMatrix(width, height)
        .also { it.addBlocks(*representation.map { it.copy() }.toTypedArray()) }
        .apply {
            val a = Random.nextInt(representation.size)
            val b = Random.nextInt(representation.size)
            representation[a].value = representation[b].value.also { representation[b].value = representation[a].value }
        }

     fun mergeRandomBlocks(): SparseMatrix? {
        val copy = SparseMatrix(width, height).also { it.addBlocks(*representation.map { it.copy() }.toTypedArray()) }
        for (randomBlock in copy.representation.shuffled()) { // yee haw O(n^2)
            for (block in copy.representation) {
                if (block != randomBlock) {
                    if (block.column == randomBlock.column && block.width == randomBlock.width && block.row + block.width == randomBlock.row) {
                        copy.representation.remove(block)
                        copy.representation.remove(randomBlock)
                        copy.representation.add(
                            SparseBlock(
                                min(randomBlock.row, block.row),
                                block.column,
                                block.width,
                                block.height + randomBlock.height,
                                arrayOf(block, randomBlock).random().value
                            )
                        )
                        return copy
                    }
                    if (block.row == randomBlock.row && block.height == randomBlock.height && block.column + block.height == randomBlock.height) {
                        copy.representation.remove(block)
                        copy.representation.remove(randomBlock)
                        copy.representation.add(
                            SparseBlock(
                                block.row,
                                min(block.column, randomBlock.column),
                                block.width + randomBlock.width,
                                block.height,
                                arrayOf(block, randomBlock).random().value
                            )
                        )
                        return copy
                    }
                }
            }
        }
        return null
    }

    private fun splitBigBlock(blockSize: Int): SparseMatrix? {
        val copy = SparseMatrix(width, height).also { it.addBlocks(*representation.map { it.copy() }.toTypedArray()) }
        for (randomBlock in copy.representation.shuffled()) with(copy) {
            if (randomBlock.width >= 2 * blockSize) {
                representation.remove(randomBlock)
                val over = randomBlock.width - 2 * blockSize
                val splitAtColumn = blockSize + if (over > 0) Random.nextInt(over) else 0

                addBlocks(
                    SparseBlock(
                        randomBlock.row,
                        randomBlock.column,
                        splitAtColumn,
                        randomBlock.height,
                        randomBlock.value
                    ),
                    SparseBlock(
                        randomBlock.row,
                        randomBlock.column + splitAtColumn,
                        randomBlock.width - splitAtColumn,
                        randomBlock.height,
                        randomBlock.value
                    )
                )

                return copy
            } else if (randomBlock.height >= 2 * blockSize) {
                representation.remove(randomBlock)

                val over = randomBlock.height - 2 * blockSize
                val splitAtRow = blockSize + if (over > 0) Random.nextInt(over) else 0

                addBlocks(
                    SparseBlock(
                        randomBlock.row,
                        randomBlock.column,
                        randomBlock.width,
                        splitAtRow,
                        randomBlock.value
                    ),
                    SparseBlock(
                        randomBlock.row + splitAtRow,
                        randomBlock.column,
                        randomBlock.width,
                        randomBlock.height - splitAtRow,
                        randomBlock.value
                    )
                )

                return copy
            }
        }
        return null
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

    override fun toString(): String {
        var result = ""
        for (row in 0 until height) {
            for (column in 0 until width) result += "${getValue(row, column)} "
            result += "\n"
        }
        return result
    }
}