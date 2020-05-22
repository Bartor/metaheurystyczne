package solver

import map.MazeMap
import solver.enums.Moves
import kotlin.random.Random

class PathGenetic(
    private val map: MazeMap,
    private val tournamentSize: Int,
    private val populationSize: Int
) {
    public fun solve(
        time: Long,
        initialPopulation: List<Path>
    ): Path {
        val start = System.nanoTime()
        var lastBest = System.nanoTime()

        var population = initialPopulation

        var best = initialPopulation[0]
        var bestEval = map.evaluatePath(best)

        while (System.nanoTime() < start + time) {
            for (p in population) {
                map.evaluatePath(p).also {
                    if (bestEval.length > it.length) {
                        best = p
                        bestEval = it
                        lastBest = System.nanoTime()
                    }
                }
            }

            if (System.nanoTime() > lastBest + time.toDouble() / 33) break

            val newPopulation = mutableListOf<Path>()
            for (i in 0 until populationSize / 2) {
                val p1 = population[selection(population)]
                val p2 = population[selection(population)]

                val (c1, c2) = p1.crossover(p2)
                newPopulation += c1.randomTweak()
                newPopulation += c2.randomTweak()
            }

            population = newPopulation
        }
        val cleanedBest = Path()

        var index = 0
        while (index < bestEval.length) {
            if (index == bestEval.length - 1) {
                cleanedBest.append(best.sequence[index])
                break
            }

            with(best.sequence) {
                if ((get(index) == Moves.L && get(index + 1) == Moves.R) ||
                    (get(index) == Moves.R && get(index + 1) == Moves.L) ||
                    (get(index) == Moves.U && get(index + 1) == Moves.D) ||
                    (get(index) == Moves.D && get(index + 1) == Moves.U)
                ) {
                    index += 2
                } else {
                    cleanedBest.append(get(index++))
                }
            }
        }

        return cleanedBest
    }

    private fun selection(population: List<Path>): Int {
        var best = Random.nextInt(population.size)
        for (i in 0 until tournamentSize) {
            Random.nextInt(population.size).also {
                if (map.evaluatePath(population[it]).length < map.evaluatePath(population[best]).length) best = it
            }
        }
        return best
    }
}