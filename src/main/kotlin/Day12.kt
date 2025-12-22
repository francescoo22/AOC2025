private val input = readRawInput().split("\n\n").let {
    val shapes = it.dropLast(1).map { rawShape ->
        rawShape.split("\n").drop(1)
    }

    val regions = it.last().split("\n").let { rawArea ->
        rawArea.map { line ->
            line.split(" ").let { splitLine ->
                val dimensions = splitLine.first().dropLast(1).split("x").let { (x, y) -> x.toInt() to y.toInt() }
                val presentsNumber = splitLine.drop(1).map { num -> num.toInt() }
                Region(dimensions, presentsNumber)
            }
        }
    }

    Input(shapes, regions)
}

private data class Input(
    val shapes: List<List<String>>,
    val regions: List<Region>
)

private data class Region(
    val dimensions: IntInt,
    val presentNumber: List<Int>,
)

private fun part1() {
    val (_, regions) = input
    val ans = regions.count { region ->
        val (dimensions, presentNumber) = region
        val area = dimensions.first * dimensions.second
        val presentsArea = presentNumber.sum() * 9
        area >= presentsArea
    }
    ans.println()
}

private fun part2() {
    println("Merry Christmas!")
}

suspend fun main() {
    withTime { part1() }
    withTime { part2() }
}