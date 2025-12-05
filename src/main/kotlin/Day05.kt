private val input = readRawInput().split("\n\n").let { (ranges, numbers) ->
    ranges.lines().map { it.split("-").let { (l,r) -> l.toLong()..r.toLong() } } to
            numbers.lines().map { it.toLong() }
}

private fun part1() {
    val (ranges, nums) = input
    val ans = nums.count { n ->
        ranges.any { n in it }
    }
    println(ans)
    check (ans == 513)
}

private fun part2() {
    val ranges = input.first.sortedBy { it.first }
    val res = mutableSetOf<LongRange>()
    var l = ranges.first().first
    var r = l
    for (range in ranges) {
        if (range.first <= r) {
            r = maxOf(r, range.last)
        }
        else {
            res += l..r
            l = range.first
            r = range.last
        }
    }
    if (l..r !in res) res += l..r

    val ans = res.sumOf { it.last - it.first + 1 }
    println(ans)
    check(ans == 339668510830757)
}

suspend fun main() {
    withTime { part1() }
    withTime { part2() }
}