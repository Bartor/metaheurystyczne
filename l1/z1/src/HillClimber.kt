import java.util.*
import kotlin.math.abs
import kotlin.math.floor

data class HCSolution(val vector: CoolVector, val value: Double)

class HillClimber(
    private val sigma: Double = 1.0,
    private val fn: (CoolVector) -> Double
) {
    fun solve(nanoseconds: Long, defaultTime: Long): HCSolution {
        val globalStart = System.nanoTime()
        val rand = Random()

        var current = CoolVector(arrayOf(.0, .0, .0, .0)).tweak()
        var best = CoolVector(current.values.copyOf())

        while ((System.nanoTime() - globalStart) < nanoseconds) {
            val localStart = System.nanoTime()
            val time = floor(abs(rand.nextGaussian()) + 1) * defaultTime

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
