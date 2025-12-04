private val input = readInput()

private fun isAccessible(i: Int, j: Int, grid: List<String> = input): Boolean {
    if (grid[i][j] != '@') return false

    return AllDirections.count { (di, dj) ->
        runIfInBounds { grid[i + di][j + dj] == '@' } ?: false
    } < 4
}

private fun part1() {
    val ans = input.withIndex().sumOf { (i, row) ->
        row.indices.count { j -> isAccessible(i, j) }
    }
    println(ans)
    check(ans == 1543)
}

private fun part2() {
    var ans = 0
    var curInput = input
    do {
        val changedPos = mutableSetOf<IntInt>()
        ans += curInput.withIndex().sumOf { (i, row) ->
            row.indices.count { j ->
                isAccessible(i, j, curInput).also { if (it) changedPos += i to j }
            }
        }
        curInput = curInput.mapIndexed { i, row ->
            row.mapIndexed { j, c -> if (i to j in changedPos) '.' else c }.joinToString("")
        }
    } while (changedPos.isNotEmpty())
    println(ans)
    check(ans == 9038)
}

suspend fun main() {
    withTime { part1() }
    withTime { part2() }
}