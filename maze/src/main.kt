import map.MazeMap
import tabu.Path
import tabu.TabuSolver
import tabu.enums.Moves

fun main() {
    val (time, rows, columns) = readLine()!!.split(' ').map { it.toInt() }
    val m = MazeMap(rows, columns)

    for (i in 0 until rows) {
        m.setRow(i, readLine()!!.split("").filter { it != "" }.map { it.toInt() }.toTypedArray())
    }

    val ts = TabuSolver(m)
    println(ts.naiveSolution())
}