private val input = readInput()

private fun solve(line: String, n: Int) = buildString {
    var remaining = line
    for (i in n - 1 downTo 0) {
        val l = remaining.dropLast(i).max()
        append(l)
        remaining = remaining.substringAfter(l)
    }
}

private fun part1() {
    val ans = input.sumOf { solve(it, 2).toInt() }
    println(ans)
    check(ans == 17330)
}

private fun part2() {
    val ans = input.sumOf { solve(it, 12).toLong() }
    println(ans)
    check(ans == 171518260283767)
}

suspend fun main() {
    withTime { part1() }
    withTime { part2() }
}