package kiwiband.dnb.math

/**
 * Class representing rectangular borders. [a.x, b.x) X [a.y, b.y)
 * It is set by top left and bottom right corners.
 * Bottom right coordinate is not inclusive.
 * For correct work, the following should be true:
 * b.x >= a.x && b.y >= a.y
 * @param a top left corner
 * @param b non-inclusive bottom right corner
 */
class Borders(val a: Vec2, val b: Vec2) {
    constructor(ax: Int, ay: Int, bx: Int, by: Int) : this(Vec2(ax, ay), Vec2(bx, by))

    /**
     * Checks if the point is in borders.
     * @param v point to check
     * @return if the point is in borders.
     */
    operator fun contains(v: Vec2): Boolean {
        return a.x <= v.x && v.x < b.x
                && a.y <= v.y && v.y < b.y
    }

    /**
     * Fits orders inside another borders.
     * @param other borders to fit in.
     * @return fitted borders.
     */
    fun fitIn(other: Borders): Borders = a.fitInIncluded(other) to b.fitInIncluded(other)

    /**
     * Checks if a predicate is true for any point in borders.
     * @param predicate
     * @return if a predicate is true for any point in borders
     */
    fun any(predicate: (Vec2) -> Boolean): Boolean {
        val v = Vec2M()
        for (y in a.y until b.y) {
            for (x in a.x until b.x) {
                if (predicate(v.set(x, y))) return true
            }
        }
        return false
    }

    /**
     * Executes a function for all points in borders.
     * @param consumer function to execute
     */
    fun forEach(consumer: (Vec2) -> Unit) {
        val v = Vec2M()
        for (y in a.y until b.y) {
            for (x in a.x until b.x) {
                consumer(v.set(x, y))
            }
        }
    }

    operator fun plus(offset: Vec2) = Borders(a + offset, b + offset)
    operator fun minus(offset: Vec2) = Borders(a - offset, b - offset)
}

