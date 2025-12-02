private val input = readInput().map {
    val sign = when (it.first()) {
        'L' -> -1
        'R' -> 1
        else -> error("incorrect input")
    }
    sign to it.drop(1).toInt()
}

private fun part1() {
    var initialPos = 50
    var ans = 0
    for ((sign, len) in input) {
        initialPos = initialPos + len * sign mod 100
        if (initialPos == 0) ans++
    }
    println(ans)
    check(ans == 1129)
}

private fun part2() {
    var initialPos = 50
    var ans = 0
    for ((sign, len) in input) {
        if (sign == -1) {
            ans += (len - initialPos + 100) / 100
            if (initialPos == 0) ans--
        }
        if (sign == 1) {
            ans += (len + initialPos) / 100
        }
        initialPos = initialPos + len * sign mod 100
    }
    println(ans)
    check(ans == 6638)
}

suspend fun main() {
    withTime { part1() }
    withTime { part2() }
}
