import map.MazeMap
import solver.Path
import solver.SimulatedAnnealing
import solver.enums.Moves
import kotlin.math.log10
import kotlin.math.min

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

    val sa = SimulatedAnnealing(m)
    val solution = sa.solve(
        time * 1000000000L,
        min(log10((time).toDouble()), log10(rows * columns / 10.0)),
        initialPath,
        200.0
    ) {
        0.999 * it
    }

    print("${solution.sequence.size} $solution")
}