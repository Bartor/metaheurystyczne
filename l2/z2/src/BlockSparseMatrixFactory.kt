import matrices.SparseBlock
import matrices.SparseMatrix
import matrices.SparseValues

class BlockSparseMatrixFactory {
    companion object {
        public fun getBlockSparseMatrix(height: Int, width: Int, blockSize: Int) = SparseMatrix(width, height).apply {
            // add regular blocks k x k
            for (row in 0 until height - blockSize - height % blockSize step blockSize)
                for (column in 0 until width - blockSize - width % blockSize step blockSize) {
                    addBlocks(SparseBlock(row, column, blockSize, blockSize, SparseValues.available.random()))
                }

            // fill the last column blocks
            for (row in 0 until height - blockSize - height % blockSize step blockSize) {
                addBlocks(
                    SparseBlock(
                        row,
                        (Math.floorDiv(width, blockSize) - 1) * blockSize,
                        blockSize + width % blockSize,
                        blockSize,
                        SparseValues.available.random()
                    )
                )
            }

            // fill the last row
            for (column in 0 until width - blockSize - width % blockSize step blockSize) {
                addBlocks(
                    SparseBlock(
                        (Math.floorDiv(height, blockSize) - 1) * blockSize,
                        column,
                        blockSize,
                        blockSize + height % blockSize,
                        SparseValues.available.random()
                    )
                )
            }

            // fill the bottom-right
            addBlocks(
                SparseBlock(
                    (Math.floorDiv(height, blockSize) - 1) * blockSize,
                    (Math.floorDiv(width, blockSize) - 1) * blockSize,
                    blockSize + width % blockSize,
                    blockSize + height % blockSize,
                    SparseValues.available.random()
                )
            )
        }
    }
}