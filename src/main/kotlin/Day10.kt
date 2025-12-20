import com.google.ortools.Loader
import com.google.ortools.linearsolver.MPSolver

private val input = readInput().map { line ->
    line.filterNot { it in "{[()]}" }.split(" ").let {
        Manual(
            config = it.first(),
            buttons = it.subList(1, it.size - 1).map { ns -> ns.split(",").map { n -> n.toInt() } },
            voltage = it.last().split(",").map { n -> n.toInt() },
        )
    }
}

private data class Manual(
    val config: String,
    val buttons: List<List<Int>>,
    val voltage: List<Int>
)

private fun allPermutations(size: Int): List<String> {
    if (size == 1) return listOf(".", "#")
    return allPermutations(size - 1).map { "$it." } + allPermutations(size - 1).map { "$it#" }
}

private fun press(c: Char) = when (c) {
    '.' -> '#'
    '#' -> '.'
    else -> error("incorrect input")
}

private fun String.press(buttons: List<Int>): String {
    val res = this.toCharArray()
    for (button in buttons) {
        res[button] = press(res[button])
    }
    return String(res)
}

private fun part1() {
    var ans = 0
    for (manual in input) {
        val (goal, buttonsList, _) = manual
        val initConfig = ".".repeat(goal.length)
        val dp = mutableMapOf(initConfig to 0)
        var i = 1
        var toVisit = setOf(initConfig)
        while (dp[goal] == null) {
            val newToVisit = mutableSetOf<String>()
            toVisit.forEach { key ->
                for (buttons in buttonsList) {
                    val new = key.press(buttons)
                    if (dp[new] == null) {
                        dp[new] = i
                        newToVisit += new
                    }
                }
            }
            toVisit = newToVisit
            i++
        }
        ans += dp[goal]!!
    }
    println(ans)
    check(ans == 375)
}

private fun solveManual(manual: Manual): Int {
    val (_, buttons, voltage) = manual

    // Required native library loading
    Loader.loadNativeLibraries()

    // Create MILP solver (SCIP backend)
    val solver = MPSolver.createSolver("SCIP")
        ?: error("Could not create solver")

    // Integer variables
    val maxVoltage = voltage.max().toDouble()
    val ps = buildList {
        for (i in buttons.indices) {
            add(solver.makeIntVar(0.0, maxVoltage, "p$i"))
        }
    }

    // constraints
    for ((i, v) in voltage.withIndex()) {
        val constraint = solver.makeConstraint(v.toDouble(), v.toDouble())
        for ((j, button) in buttons.withIndex()) {
            if (i in button) constraint.setCoefficient(ps[j], 1.0)
        }
    }

    // Objective
    val objective = solver.objective()
    for (p in ps) objective.setCoefficient(p, 1.0)
    objective.setMinimization()

    // Solve
    val status = solver.solve()
    if (status != MPSolver.ResultStatus.OPTIMAL) error("Could not find optimal solution")
    return objective.value().toInt()
}

private fun part2() {
    val ans = input.sumOf { solveManual(it) }
    println(ans)
    check(ans == 15377)
}

suspend fun main() {
    withTime { part1() }
    withTime { part2() }
}