import tabu.Distances
import tabu.TabuTSP

fun main() {
    val (time, cities) = readLine()!!.split(' ').map { it.toInt() }

    val d = Distances(cities)
    for (i in 0 until cities) {
        d.setCity(i, readLine()!!.split(' ').map { it.toInt() }.toTypedArray())
    }

    val tabu = TabuTSP(d, cities * 10, cities * cities)
    val solution = tabu.solve(time * 1000000000L, d.naiveSolution())
    println(solution.length())
    System.err.println(solution)
}