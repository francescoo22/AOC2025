import kotlin.math.abs

private val input = readInput().map { line ->
    line.split(',').let { (x, y) -> x.toLong() to y.toLong() }
}

private val inputDouble = readInput().map { line ->
    line.split(',').let { (x, y) -> x.toDouble() to y.toDouble() }
}

private fun area(p1: Pair<Long, Long>, p2: Pair<Long, Long>) =
    (abs(p1.first - p2.first) + 1) * (abs(p1.second - p2.second) + 1)

private fun part1() {
    val ans = input.uniquePairs().maxOf { (p1, p2) ->
        area(p1, p2)
    }
    check(ans == 4755064176)
    println(ans)
}

private fun polygonSegments(polygon: List<Point2D>): List<Segment> =
    polygon.withIndex().map { (i, point) ->
        point to polygon[(i + 1) % polygon.size]
    }

private fun polygonArea(polygon: List<Point2D>): Double =
    polygonSegments(polygon).sumOf { (p1, p2) ->
        (p2.first - p1.first) * p1.second // they are always rectangles
    }

private fun isClockwise(polygon: List<Point2D>): Boolean {
    val area = polygonArea(polygon)
    return area > 0
}

private fun outerPolygon(innerPolygon: List<Point2D>, isClockwise: Boolean): List<Point2D> {
    if (!isClockwise) {
        return buildList {
            for ((i, point) in innerPolygon.withIndex()) {
                val nextPoint = innerPolygon[(i + 1) mod innerPolygon.size]
                val prevPoint = innerPolygon[(i - 1) mod innerPolygon.size]

                if (prevPoint.first != point.first) {
                    val dx = if (nextPoint.second < point.second) -0.1 else +0.1
                    val dy = if (prevPoint.first < point.first) -0.1 else +0.1
                    add(point.first + dx to point.second + dy)
                } else {
                    val dx = if (prevPoint.second < point.second) +0.1 else -0.1
                    val dy = if (nextPoint.first < point.first) +0.1 else -0.1
                    add(point.first + dx to point.second + dy)
                }
            }
        }
    } else {
        error("Non ho voglia")
    }
}

private fun rectangleSegments(p1: Point2D, p2: Point2D): List<Segment> = listOf(
    p1 to (p1.first to p2.second),
    p1 to (p2.first to p1.second),
    p2 to (p1.first to p2.second),
    p2 to (p2.first to p1.second),
)

private fun isContained(p1: Point2D, p2: Point2D, polygonSegments: List<Segment>): Boolean {
    val rectangleSegments = rectangleSegments(p1, p2)
    return !polygonSegments.any { polygonSegment ->
        rectangleSegments.any { rectangleSegment ->
            intersect(polygonSegment, rectangleSegment)
        }
    }
}

private fun part2() {
    val isClockwise = isClockwise(inputDouble)

    val outerPolygon = outerPolygon(inputDouble, isClockwise)
    val outerPolygonSegments = polygonSegments(outerPolygon)

    val ans = inputDouble.uniquePairs().filter { (p1, p2) ->
        isContained(p1, p2, outerPolygonSegments)
    }.maxOf { (p1, p2) ->
        area(p1.first.toLong() to p1.second.toLong(), p2.first.toLong() to p2.second.toLong())
    }
    ans.println()
    check(ans == 1613305596L)
}

suspend fun main() {
    withTime { part1() }
    withTime { part2() }
}