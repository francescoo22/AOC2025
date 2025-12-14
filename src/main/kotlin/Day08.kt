private val input =
    readInput().map { it.split(",").let { (x, y, z) -> Point3D(x.toDouble(), y.toDouble(), z.toDouble()) } }

private val sortedInputPairs = input.withIndex().toList().uniquePairs().map { (indexedP1, indexedP2) ->
    val (i1, p1) = indexedP1
    val (i2, p2) = indexedP2
    p1.distanceTo(p2) to (i1 to i2)
}.sortedBy { it.first }.map { it.second }

fun buildGraph(size: Int) = sortedInputPairs.take(size).let { pairs ->
    buildMap {
        for ((i, j) in pairs) {
            getOrPut(i) { mutableSetOf() } += j
            getOrPut(j) { mutableSetOf() } += i
        }
    }
}

private fun part1() {
    val graph = buildGraph(1000)

    val set = (List(input.size) { it }).toMutableSet()
    val componentSizes = mutableListOf<Int>()

    fun visit(i: Int): Int {
        if (i !in set) return 0
        set -= i
        return 1 + (graph[i]?.sumOf { visit(it) } ?: 0)
    }

    while (set.isNotEmpty()) {
        componentSizes += visit(set.first())
    }

    val ans = componentSizes.sortedBy { -it }.take(3).fold(1) { a, b -> a * b }
    println(ans)
    check(ans == 50760)
}

private fun part2() {
    var set = (List(input.size) { it }).toMutableSet()
    var ans = 0L

    var cur = 4498
    while (true) {
        val graph = buildGraph(cur)
        fun visit(i: Int) {
            if (i !in set) return
            set -= i
            graph[i]?.forEach { visit(it) }
        }
        visit(0)
        if (set.isEmpty()) {
            val (i1, i2) = input.withIndex().toList().uniquePairs().asSequence().map { (indexedP1, indexedP2) ->
                val (_, p1) = indexedP1
                val (_, p2) = indexedP2
                p1.distanceTo(p2) to (p1 to p2)
            }.sortedBy { it.first }.map { it.second }.take(cur).last()

            ans = i1.x.toLong() * i2.x.toLong()
            break
        }
        set = (List(input.size) { it }).toMutableSet()
        cur++
    }

    println(ans)
    check(ans == 3206508875)

}

suspend fun main() {
    withTime { part1() }
    withTime { part2() }
}