import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sqrt

fun salomon(value: CoolVector) = 1 - cos(2* PI* sqrt(value.values.sumByDouble { it*it })) + 0.1 * sqrt(value.values.sumByDouble { it * it })

fun main(args: Array<String>) {
    val (maxTime, x1, x2, x3, x4) = readLine()!!.split(' ').map { it.toDouble() }
    val initialVector = CoolVector(arrayOf(x1, x2, x3, x4))

    val annealing = SimulatedAnnealing { salomon(it) }
    val result = annealing.solve(
        (maxTime * 1000000000).toLong(),
        initialVector,
        200.0
    ) { 0.5 * it }

    println("$result ${salomon(result)}")
}