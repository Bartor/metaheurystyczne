import map.MazeMap
import solver.Path
import solver.PathGenetic
import solver.enums.Moves

fun main() {
    val (time, rows, columns, s, populationSize) = readLine()!!.split(' ').map { it.toInt() }
    val m = MazeMap(rows, columns)

    for (i in 0 until rows) {
        m.setRow(i, readLine()!!.split("").filter { it != "" }.map { it.toInt() }.toTypedArray())
    }

    val initialPopulation = mutableListOf<Path>()
    for (i in 0 until s) {
        initialPopulation.add(Path().apply {
            append(*readLine()!!.split("").filter { it != "" }.map {
                when (it.toUpperCase()) {
                    "U" -> Moves.U
                    "D" -> Moves.D
                    "L" -> Moves.L
                    "R" -> Moves.R
                    else -> throw Exception("Move not recognized: $it")
                }
            }.toTypedArray())
        })
    }

    val solver = PathGenetic(m, if (populationSize/2 > 1) populationSize/2 else 2, populationSize)
    val res = solver.solve(
        time * 1000000000L,
        initialPopulation
    )

    println(m.evaluatePath(res).length)
    System.err.println(res.sequence.joinToString(""))
}