import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sqrt

fun main(args: Array<String>) {
    if (args.size != 2) {
        error("Usage: [time in seconds] [0 for happyCat, 1 for griewank]")
    }

    val time = args[0].toLong()
    val hc = when (args[1]) {
        "0" -> HillClimber(0.0001) { happyCat(it) }
        "1" -> HillClimber(0.0001) { griewank(it) }
        else -> error("Second argument should be 0 or 1")
    }

    val solution = hc.solve(time * 1000000000, time * 10000000)
    println("${solution.vector} ${solution.value}")
}

fun happyCat(coolVector: CoolVector) =
    (coolVector.norm().pow(2.0) - 4.0).pow(2.0).pow(0.125) + (0.25) * ((0.5) * coolVector.norm().pow(2.0) + coolVector.values.sum()) + 0.5

fun griewank(coolVector: CoolVector) =
    1.0 + coolVector.values.reduce { acc, d -> acc + d.pow(2.0) } / 4000 - coolVector.values.foldIndexed(1.0) { index, acc, d -> acc * cos(d / sqrt(index + 1.0)) }
