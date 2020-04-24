package matrices

data class SparseBlock(
    val row: Int,
    val column: Int,
    val width: Int,
    val height: Int,
    val value: Short
)