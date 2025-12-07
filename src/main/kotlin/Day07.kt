private val input = readInput()

private fun part1() {
    val beams = mutableSetOf(input.first().indexOfFirst { it == 'S' })
    var ans = 0
    for (i in 2..<input.size step 2) {
        val split = mutableSetOf<Int>()
        for (beam in beams) {
            if (input[i][beam] == '^') split += beam
        }
        ans += split.size
        beams -= split
        beams += split.map { it + 1 } + split.map { it - 1 }
    }
    println(ans)
    check(ans == 1615)
}

private val dp: MutableList<MutableList<Long?>> = MutableList(input.size) {
    MutableList(input.first().length) {
        null
    }
}

private fun solve2(i: Int, beam: Int): Long {
    if (i == input.size) return 1
    dp[i][beam]?.let { return it }
    val res = if (input[i][beam] == '^') solve2(i + 2, beam - 1) + solve2(i + 2, beam + 1) else solve2(i + 2, beam)
    dp[i][beam] = res
    return res
}

private fun part2() {
    val ans = solve2(2, input.first().indexOfFirst { it == 'S' })
    println(ans)
    check(ans == 43560947406326)
}

suspend fun main() {
    withTime { part1() }
    withTime { part2() }
}