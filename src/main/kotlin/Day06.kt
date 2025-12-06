private val input = readInput().let { lines ->
    lines.dropLast(1).map { it.split(Regex("\\s+")).map { n -> n.toLong() } } to
            lines.last().split(Regex("\\s+")).map { when(it) {
                "+" -> 0L
                "*" -> 1L
                else -> error("Invalid input")
            } }
}

private fun part1() {
    val (nums, ops) = input
    val res = ops.toMutableList()
    for (line in nums) {
        for ((i, n) in line.withIndex()) {
            if(ops[i] == 0L) res[i] += n
            else res[i] *= n
        }
    }
    res.sum().println()
}

private fun part2() {
    val input2 = readInputNoTrim().dropLast(1)
    val len = input2.first().length
    val ops = input.second
    var curOpIndex = ops.size - 1
    var ans = 0L
    var curRes = ops.last()
    for (i in len - 1 downTo 0) {
        val curNum = buildString {
            for (line in input2.dropLast(1)) append(line[i])
        }.trim().toIntOrNull()

        if (curNum != null) {
            if (ops[curOpIndex] == 0L) curRes += curNum
            else curRes *= curNum
        } else {
            ans += curRes
            curOpIndex--
            curRes = ops[curOpIndex]
        }
    }
    ans += curRes
    println(ans)
}

suspend fun main() {
    withTime { part1() }
    withTime { part2() }
}