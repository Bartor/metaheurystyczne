package tabu

import map.MazeMap
import map.enums.FieldType
import tabu.enums.Moves
import tabu.enums.PathState
import java.util.*

class TabuSolver(
    private val map: MazeMap,
    private val maxTabuLen: Int,
    private val maxTweaks: Int
) {
    fun naiveSolution(): Path {
        val path = Path()

        var direction = Moves.values().random()
        var evaluation = map.evaluatePath(path)
        while (evaluation.winningMove() == null) {
            evaluation = map.evaluatePath(path)

            if (evaluation.fields[direction] == FieldType.WALL) {
                direction = Moves.values().find { it.value == (direction.value + 1).rem(4) }!!
                continue
            }

            path.append(direction)
        }

        return path.append(evaluation.winningMove()!!.key)
    }

    fun solve(nanoseconds: Long, startingPath: Path): Path {
        val queue = LinkedList<Path>()

        var current = startingPath
        var best = Path(mutableListOf(*startingPath.sequence.toTypedArray()))

        queue.push(current)

        val startTime = System.nanoTime()
        while ((startTime + nanoseconds) > System.nanoTime()) {
            if (queue.size > maxTabuLen) queue.remove()

            var tweaked = current.randomTweak()

            for (iteration in 0 until maxTweaks) {
                if ((startTime + nanoseconds) < System.nanoTime()) break

                val evenBetterTweak = current.randomTweak()

                if (queue.none { it == evenBetterTweak } && (betterQuality(
                        evenBetterTweak,
                        tweaked
                    ) || queue.any { it == tweaked })) {
                    tweaked = evenBetterTweak
                }
            }

            if (queue.none { it == tweaked }) {
                current = tweaked
                queue.push(tweaked)
            }

            if (betterQuality(current, best)) best = current
        }

        val bestEval = map.evaluatePath(best)
        val cleanedBest = Path(mutableListOf())

        var index = 0
        while (index < bestEval.length) {
            if (index == bestEval.length - 1) {
                cleanedBest.append(best.sequence.last())
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
                    cleanedBest.append(get(index))
                    index++
                }
            }
        }
        return cleanedBest
    }

    private fun betterQuality(a: Path, b: Path): Boolean {
        val aEval = map.evaluatePath(a)
        val bEval = map.evaluatePath(b)

        return if (aEval.state == PathState.WINNING) {
            if (bEval.state == PathState.WINNING) {
                aEval.length < bEval.length
            } else true
        } else false
    }
}