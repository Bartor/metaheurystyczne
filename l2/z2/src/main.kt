import matrices.FullMatrix
import matrices.SparseBlock
import matrices.SparseMatrix

fun main() {
    val (time, height, width, blockSize) = readLine()!!.split(' ').map { it.toInt() }
    val inputMatrix = FullMatrix(width, height)
    for (row in 0 until height) {
        inputMatrix.setRow(row, readLine()!!.split(' ').map { it.toShort() }.toTypedArray())
    }
    val zeroMatrix = FullMatrix(width, height, true)
    val sparseZero = SparseMatrix(width, height).apply {
        addBlock(SparseBlock(0, 0, width, height, 64))
    }

    println(inputMatrix.distance(zeroMatrix))
    println(sparseZero.distance(zeroMatrix))
    println(sparseZero.distance(inputMatrix))
}