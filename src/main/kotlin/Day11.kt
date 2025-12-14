import java.math.BigInteger

private val input = buildMap {
    readInput().forEach { str ->
        str.split(" ").let {
            this[it.first().dropLast(1)] = it.drop(1)
        }
    }
}

private val pathsCache = mutableMapOf<Pair<String, String>, BigInteger>()
private fun paths(from: String, to: String) : BigInteger {
    if (from == to) return BigInteger.ONE
    pathsCache[from to to]?.let { return it }

    return input[from]?.sumOf { newFrom ->
        pathsCache[newFrom to to] = paths(newFrom, to)
        pathsCache[newFrom to to]!!
    } ?: 0.toBigInteger()
}

private fun part1() {
    val ans = paths("you", "out")
    println(ans)
    check(ans == 634.toBigInteger())
}

private fun part2() {
    val ans =  paths("svr", "dac") * paths("dac", "fft") * paths("fft", "out") +
            paths("svr", "fft") * paths("fft", "dac") * paths("dac", "out")
    println(ans)
    check(ans == 377452269415704.toBigInteger())
}

suspend fun main() {
    withTime { part1() }
    withTime { part2() }
}