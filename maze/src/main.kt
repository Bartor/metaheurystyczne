import map.MazeMap
import tabu.TabuSolver

fun main() {
    val (time, rows, columns) = readLine()!!.split(' ').map { it.toInt() }
    val m = MazeMap(rows, columns)

    for (i in 0 until rows) {
        m.setRow(i, readLine()!!.split("").filter { it != "" }.map { it.toInt() }.toTypedArray())
    }

    val ts = TabuSolver(m, 10000, 100)
    val naive = ts.naiveSolution()
    println(ts.solve(time * 1000000000L, naive))
}