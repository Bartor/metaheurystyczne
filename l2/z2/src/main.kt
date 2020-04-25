import matrices.FullMatrix

fun main() {
    val input = readLine()!!.split(' ').toMutableList()
    val time = input.removeFirst().toDouble()
    val (height, width, blockSize) = input.map { it.toInt() }
    val inputMatrix = FullMatrix(width, height).apply {
        for (row in 0 until height) setRow(row, readLine()!!.split(' ').map { it.toShort() }.toTypedArray())
    }

    val randomSparse = BlockSparseMatrixFactory.getBlockSparseMatrix(height, width, blockSize)

    val solution = SparseMatrixAnnealing { it.distance(inputMatrix) }.solve(
        (time * 1e9).toLong(),
        blockSize,
        randomSparse,
        200.0
    ) { 0.5 * it }

    println(solution.distance(inputMatrix))
    System.err.println(solution)
}