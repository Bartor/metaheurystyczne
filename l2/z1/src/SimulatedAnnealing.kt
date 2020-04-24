import kotlin.math.exp
import kotlin.random.Random

class SimulatedAnnealing(
    private val tweakSigma: Double = 1.0,
    private val fn: (CoolVector) -> Double
) {
    fun solve(
        time: Long,
        initialValue: CoolVector,
        initialTemperature: Double,
        temperatureChanger: (Double) -> Double
        ): CoolVector {
        val start = System.nanoTime()

        var best = initialValue
        var current = initialValue
        var temperature = initialTemperature

        while (System.nanoTime() < start + time) {
            val tweaked = current.tweak(tweakSigma)

            if (fn(tweaked) < fn(current) || Random.nextDouble() < exp((fn(current) -  fn(tweaked)) / temperature)) {
                current = tweaked
            }

            temperature = temperatureChanger(temperature)
            if (fn(current) < fn(best)) {
                best = current
            }
        }

        return best
    }
}