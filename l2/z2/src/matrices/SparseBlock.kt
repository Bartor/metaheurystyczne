package matrices

data class SparseBlock(
    val row: Int,
    val column: Int,
    val width: Int,
    val height: Int,
    var value: Short
)