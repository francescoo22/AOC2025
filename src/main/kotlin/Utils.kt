import java.math.BigInteger
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

val NorthSouthWestEast = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)
val AllDirections = NorthSouthWestEast + listOf(1 to 1, -1 to -1, -1 to 1, 1 to -1)

fun readInputNoTrim() = Path("src/input.txt").readText().lines()
fun readRawInput() = Path("src/input.txt").readText().trim()
fun readInput() = readRawInput().lines()

typealias IntMatrix = List<List<Int>>
typealias IntInt = Pair<Int, Int>

fun <T> List<List<T>>.forEachIndexed2(block: (Int, Int, T) -> Unit) {
    forEachIndexed { i, xs ->
        xs.forEachIndexed { j, x ->
            block(i, j, x)
        }
    }
}

fun List<String>.forEachIndexedS(block: (Int, Int, Char) -> Unit) {
    forEachIndexed { i, xs ->
        xs.forEachIndexed { j, x ->
            block(i, j, x)
        }
    }
}

fun Any.println() = println(this)

suspend fun withTime(block: suspend () -> Unit) {
    measureTimeMillis {
        block()
    }.also {
        println("Execution time: $it ms")
    }
}

fun <T> List<T>.uniquePairs() = withIndex().flatMap { (index, a) ->
    drop(index + 1).map { b -> a to b }
}

fun <T> runIfInBounds(block: () -> T): T? {
    try {
        return block()
    } catch (_: IndexOutOfBoundsException) {
    }
    return null
}

infix fun Int.mod(b: Int): Int = ((this % b) + b) % b

fun directionFromChar(c: Char) =
    when (c) {
        '>' -> 0 to 1
        '<' -> 0 to -1
        '^' -> -1 to 0
        'v' -> 1 to 0
        else -> throw IllegalArgumentException(
            "Expected one of the following chars to be converted into a direction: '<', '>', '^', 'v', but got $c"
        )
    }

fun charFromDirection(dir: IntInt) =
    when (dir) {
        0 to 1 -> '>'
        0 to -1 -> '<'
        -1 to 0 -> '^'
        1 to 0 -> 'v'
        else -> throw IllegalArgumentException(
            "Expected one of the following directions to be converted into a char: (0,1), (0,-1), (-1,0), (1,0), but got $dir)"
        )
    }

infix fun IntInt.plus(other: IntInt): IntInt = first + other.first to second + other.second

fun manhattan(a: IntInt, b: IntInt): Int = abs(a.first - b.first) + abs(a.second - b.second)

val factorial = mutableListOf(BigInteger.ONE)

fun factorial(n: Int): BigInteger {
    require(n >= 0) { "Factorial is not defined for negative numbers." }
    if (n < factorial.size) return factorial[n]
    var result = factorial.last()
    for (i in factorial.size..n) {
        result = result.multiply(BigInteger.valueOf(i.toLong()))
        factorial.add(result)
    }
    return result
}

fun bin(a: Int, b: Int) = factorial(a) / (factorial(b) * factorial(a - b))

data class Point3D(val x: Double, val y: Double, val z: Double)

fun Point3D.distanceTo(other: Point3D): Double {
    val dx = this.x - other.x
    val dy = this.y - other.y
    val dz = this.z - other.z
    return sqrt(dx.pow(2) + dy.pow(2) + dz.pow(2))
}

typealias Point2D = Pair<Double, Double>
typealias Segment = Pair<Point2D, Point2D>
fun intersect(s1: Segment, s2: Segment): Boolean {
    fun orientation(a: Point2D, b: Point2D, c: Point2D): Int {
        val value = (b.second - a.second) * (c.first - b.first) - (b.first - a.first) * (c.second - b.second)
        val eps = 1e-9
        return when {
            value > eps -> 1
            value < -eps -> -1
            else -> 0
        }
    }

    fun onSegment(a: Point2D, b: Point2D, c: Point2D): Boolean {
        // c lies on segment ab assuming colinearity
        val (ax, ay) = a
        val (bx, by) = b
        val (cx, cy) = c
        val minX = minOf(ax, bx) - 1e-9
        val maxX = maxOf(ax, bx) + 1e-9
        val minY = minOf(ay, by) - 1e-9
        val maxY = maxOf(ay, by) + 1e-9
        return cx in minX..maxX && cy in minY..maxY
    }

    val (p1, q1) = s1
    val (p2, q2) = s2

    val o1 = orientation(p1, q1, p2)
    val o2 = orientation(p1, q1, q2)
    val o3 = orientation(p2, q2, p1)
    val o4 = orientation(p2, q2, q1)

    // General case
    if (o1 != o2 && o3 != o4) return true

    // Special Cases - colinear and on segment (inclusive of endpoints)
    if (o1 == 0 && onSegment(p1, q1, p2)) return true
    if (o2 == 0 && onSegment(p1, q1, q2)) return true
    if (o3 == 0 && onSegment(p2, q2, p1)) return true
    if (o4 == 0 && onSegment(p2, q2, q1)) return true

    return false
}