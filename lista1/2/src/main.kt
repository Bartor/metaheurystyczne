import tabuTSP.Distances
import tabuTSP.Path
import tabuTSP.TabuTSP

fun main() {
    val (time, cities) = readLine()!!.split(' ').map { it.toInt() }

    val d = Distances(cities)
    for (i in 0 until cities) {
        d.setCity(i, readLine()!!.split(' ').map { it.toInt() }.toTypedArray())
    }

    val tabu = TabuTSP(d, 1000, 10000)
    val solution = tabu.solve(time * 1000000000L, d.naiveSolution())
    println(solution.length())
    System.err.println(solution)
}