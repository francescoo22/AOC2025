private val input = readInput().first().split(',').map {
    it.split('-').let { (a, b) ->
        a.toLong()..b.toLong()
    }
}

private fun invalid1(n: Long): Boolean {
    val s = n.toString()
    return s == s.take(s.length / 2).repeat(2)
}

private fun invalid2(n: Long): Boolean {
    val s = n.toString()
    return (1..s.length / 2).any {
        s.take(it).repeat(s.length / it) == s
    }
}

private fun part1() {
    val ans = input.flatten().filter(::invalid1).sum()
    println(ans)
    check(ans == 38310256125)
}

private fun part2() {
    val ans = input.flatten().filter(::invalid2).sum()
    println(ans)
    check(ans == 58961152806)
}

suspend fun main() {
    withTime { part1() }
    withTime { part2() }
}