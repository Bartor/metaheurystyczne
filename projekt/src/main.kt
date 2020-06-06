import map.MazeMap
import solver.Path
import solver.enums.Moves

fun main() {
    val (time, rows, columns) = readLine()!!.split(' ').map { it.toInt() }
    val m = MazeMap(rows, columns)

    for (i in 0 until rows) {
        m.setRow(i, readLine()!!.split("").filter { it != "" }.map { it.toInt() }.toTypedArray())
    }

    val initialPath = Path(readLine()!!.split("").filter { it != "" }.map {
        when (it) {
            "U" -> Moves.U
            "D" -> Moves.D
            "R" -> Moves.R
            "L" -> Moves.L
            else -> throw Exception("Incorrect move: $it")
        }
    })

    val eval = m.evaluatePath(initialPath)
    print(eval)
}