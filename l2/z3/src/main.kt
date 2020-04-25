import map.MazeMap
import solver.PathAnnealing

fun main() {
    val (time, rows, columns) = readLine()!!.split(' ').map { it.toInt() }
    val m = MazeMap(rows, columns)

    for (i in 0 until rows) {
        m.setRow(i, readLine()!!.split("").filter { it != "" }.map { it.toInt() }.toTypedArray())
    }

    val solver = PathAnnealing(m)

    val naive = solver.naiveSolution()
    val e = m.evaluatePath(naive)
    println(e.state)
    println(naive.sequence.size)

    val best = solver.solve(
        (time * 1000000000L),
        naive,
        1e8
    ) { 0.99 * it }

    println(best.sequence.size)
    System.err.println(best.sequence.joinToString(""))
}