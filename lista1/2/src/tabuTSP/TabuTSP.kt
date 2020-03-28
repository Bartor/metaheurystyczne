package tabuTSP

import java.util.*

class TabuTSP(
    private val distances: Distances,
    private val maxTabuLen: Int, private val
    numberOfTweaks: Int
) {
    fun solve(
        nanoseconds: Long,
        startingPath: Path = Path( // randomly shuffled path
            IntArray(distances.citiesCount) { it }.toMutableList().shuffled().toTypedArray(),
            distances
        )
    ): Path {
        val queue = LinkedList<Path>()

        var current = startingPath
        var best = Path(current.cities.copyOf(), distances)

        val startTime = System.nanoTime()
        while ((startTime + nanoseconds) > System.nanoTime()) {
            if (queue.size > maxTabuLen) queue.remove()

            var tweaked = current.randomTweak()

            for (iteration in 0 until numberOfTweaks) {
                if ((startTime + nanoseconds) < System.nanoTime()) break

                val evenBetterTweak = current.randomTweak()

                if (queue.none { it == evenBetterTweak } && (evenBetterTweak.length() < tweaked.length() || queue.any { it == tweaked })) {
                    tweaked = evenBetterTweak
                }
            }

            if (queue.none { it == tweaked }) {
                current = tweaked
                queue.push(current)
            }

            if (current.length() < best.length()) best = current
        }

        return best
    }
}