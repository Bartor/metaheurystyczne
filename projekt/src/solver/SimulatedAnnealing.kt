package solver

import map.MazeMap
import solver.enums.Moves
import kotlin.math.exp
import kotlin.random.Random

class SimulatedAnnealing(
    private val map: MazeMap
) {
    public fun solve(
        time: Long,
        threshold: Double,
        initialSolution: Path,
        initialTemperature: Double,
        temperatureChanger: (Double) -> Double
    ): Path {
        val start = System.nanoTime()
        var lastBest = System.currentTimeMillis()

        var best = map.evaluatePath(initialSolution)
        var current = map.evaluatePath(initialSolution)
        var temperature = initialTemperature

        while (System.nanoTime() < start + time) {
            val tweaked = map.evaluatePath(current.randomTweak())

            if (tweaked.sequence.isNotEmpty() &&
                (tweaked.sequence.size < current.sequence.size
                        || Random.nextDouble() < exp((current.sequence.size - tweaked.sequence.size) / temperature))
            ) {
                current = tweaked
            }

            temperature = temperatureChanger(temperature)
            if (current.sequence.isNotEmpty() && current.sequence.size < best.sequence.size) {
                best = current
                lastBest = System.currentTimeMillis()
            }

            if (lastBest.toDouble() / System.currentTimeMillis() < threshold) break
        }

        val cleanedBest = mutableListOf<Moves>()
        var index = 0
        while (index < best.sequence.size) {
            if (index == best.sequence.size - 1) {
                cleanedBest += best.sequence[index]
                break
            }

            with(best.sequence) {
                if ((get(index) == Moves.L && get(index + 1) == Moves.R) ||
                    (get(index) == Moves.R && get(index + 1) == Moves.L) ||
                    (get(index) == Moves.U && get(index + 1) == Moves.D) ||
                    (get(index) == Moves.D && get(index + 1) == Moves.U)
                ) {
                    index++
                } else {
                    cleanedBest += get(index)
                    index++
                }
            }
        }
        return Path(cleanedBest)
    }
}
