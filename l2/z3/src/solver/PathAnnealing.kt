package solver

import map.MazeMap
import map.enums.FieldType
import solver.enums.Moves
import kotlin.math.exp
import kotlin.random.Random

class PathAnnealing(
    private val map: MazeMap
) {
    fun naiveSolution(): Path {
        var path = Path()

        var direction = Moves.values().random()
        var evaluation = map.evaluatePath(path)
        while (evaluation.winningMove() == null) {
            if (evaluation.fields[direction] == FieldType.WALL || Random.nextDouble() < 0.25) {
                direction = Moves.values().filter { it.value != direction.value }.random()
                continue
            }

            path.append(direction)
            if (path.sequence.size > map.columns * map.rows / 3) { // reset on path too long
                path = Path()
            }
            evaluation = map.evaluatePath(path)
        }

        return path.append(evaluation.winningMove()!!.key)
    }

    public fun solve(
        time: Long,
        initialSolution: Path,
        initialTemperature: Double,
        temperatureChanger: (Double) -> Double
    ): Path {
        val start = System.nanoTime()

        var best = initialSolution
        var bestEval = map.evaluatePath(best)
        var current = initialSolution
        var currentEval = map.evaluatePath(current)
        var temperature = initialTemperature

        while (System.nanoTime() < start + time) {
            val tweaked = current.randomTweak()
            val tweakedEval = map.evaluatePath(tweaked)

            if (tweakedEval.length < currentEval.length
                || Random.nextDouble() < exp((currentEval.length - tweakedEval.length) / temperature)
            ) {
                current = tweaked
                currentEval = tweakedEval
            }

            temperature = temperatureChanger(temperature)
            if (currentEval.length < bestEval.length) {
                best = current
                bestEval = currentEval
            }
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
                    index++
                } else {
                    cleanedBest.append(get(index))
                    index++
                }
            }
        }
        return cleanedBest
    }
}