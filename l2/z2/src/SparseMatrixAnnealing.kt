import matrices.CoolMatrix
import matrices.SparseMatrix
import kotlin.math.exp
import kotlin.random.Random

class SparseMatrixAnnealing(
    private val qualityFn: (CoolMatrix) -> Double
) {
    public fun solve(
        time: Long,
        blockSize: Int,
        initialMatrix: SparseMatrix,
        initialTemperature: Double,
        temperatureChanger: (Double) -> Double
    ): SparseMatrix {
        val start = System.nanoTime()

        var best = initialMatrix
        var current = initialMatrix
        var temperature = initialTemperature

        while (System.nanoTime() < start + time) {
            val tweaked = current.tweak(blockSize)

            if (qualityFn(tweaked) <= qualityFn(current)
                || Random.nextDouble() < exp((qualityFn(current) - qualityFn(tweaked)) / temperature)
            ) {
                current = tweaked
            }

            temperature = temperatureChanger(temperature)
            if (qualityFn(current) <= qualityFn(best)) {
                best = current
            }
        }

        return best
    }
}