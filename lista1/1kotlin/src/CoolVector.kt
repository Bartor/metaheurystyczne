import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

class CoolVector(val values: Array<Double>) {
    companion object {
        private val random = Random()
        fun nextGaussian() = random.nextGaussian()
    }

    fun norm() = sqrt(values.map { it.pow(2) }.sum())
    fun tweak(sigma: Double = 1.0) = CoolVector(values.copyOf().map { it + nextGaussian() * sigma }.toTypedArray())

    override fun toString(): String = values.joinToString(" ")
}