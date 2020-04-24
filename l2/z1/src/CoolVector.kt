import java.util.*

class CoolVector(val values: Array<Double>) {
    companion object {
        private val random = Random()
        fun nextGaussian() = random.nextGaussian()
    }

    fun tweak(sigma: Double = 1.0) = CoolVector(values.copyOf().map { it * (1 + nextGaussian() * sigma) }.toTypedArray())

    override fun toString(): String = values.joinToString(" ")
}
