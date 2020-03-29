data class HCSolution(val vector: CoolVector, val value: Double)

class HillClimber(
    private val sigma: Double = 1.0,
    private val fn: (CoolVector) -> Double
) {
    fun solve(nanoseconds: Long): HCSolution {
        val possibleTimes = arrayOf(nanoseconds / 10000) //todo make it make sense
        val globalStart = System.nanoTime()

        var current = CoolVector(arrayOf(.0, .0, .0, .0)).tweak()
        var best = CoolVector(current.values.copyOf())

        while ((System.nanoTime() - globalStart) < nanoseconds) {
            val localStart = System.nanoTime()

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
            current = CoolVector(arrayOf(.0, .0, .0, .0)).tweak()
        }

        return HCSolution(best, fn(best))
    }
}
