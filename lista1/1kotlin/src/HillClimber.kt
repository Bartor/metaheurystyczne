import kotlin.math.pow

data class HCSolution(val vector: CoolVector, val value: Double)

class HillClimber(
    private val sigma: Double = 1.0,
    private val fn: (CoolVector) -> Double
) {
    fun solve(nanoseconds: Long): HCSolution {
        val possibleTimes = arrayOf(1000.0)
        val globalStart = System.nanoTime()

        var current = CoolVector(arrayOf(-.5, -.5, -.5, -.5)).tweak(0.5)
        var best = CoolVector(current.values.copyOf())

        while ((System.nanoTime() - globalStart) < nanoseconds) {
            val localStart = System.nanoTime()
            if (localStart > globalStart + nanoseconds) break

            val time = try {
                possibleTimes.filter { localStart + it < globalStart + nanoseconds }.random()
            } catch (e: Exception) {
                break
            }

            while ((System.nanoTime() - localStart) < time) {
                val tweaked = current.tweak(sigma)
                if (fn(tweaked) < fn(current)) current = tweaked
            }

            if (fn(current) < fn(best)) best = current
        }

        return HCSolution(best, fn(best))
    }
}
