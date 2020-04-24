package matrices

import java.lang.Exception

class FullMatrix(
    private val width: Int,
    private val height: Int,
    initZero: Boolean = false
) : CoolMatrix {
    private val representation: Array<Array<Short>> =
        Array(height) { if (initZero) Array(width) { 0.toShort() } else emptyArray() }

    public fun setRow(row: Int, values: Array<Short>) {
        if (values.size == width) representation[row] = values
        else throw Exception("Row of size ${values.size} cannot be inserted into matrix of $width width")
    }

    override fun distance(other: CoolMatrix): Double {
        var sum = 0.0
        for (row in 0 until height) for (column in 0 until width) {
            sum += (representation[row][column] - other.getValue(row, column)).let { it * it }
        }

        return sum / (width * height)
    }

    override fun getValue(row: Int, column: Int) = representation[row][column]
}