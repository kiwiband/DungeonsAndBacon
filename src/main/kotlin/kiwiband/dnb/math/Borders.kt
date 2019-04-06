package kiwiband.dnb.math

// [a.x, b.x) X [a.y, b.y)
// b.x >= a.x && b.y >= a.y for correct work
class Borders(val a: Vec2, val b: Vec2) {
    constructor(ax: Int, ay: Int, bx: Int, by: Int) : this(Vec2(ax, ay), Vec2(bx, by))

    operator fun contains(v: Vec2): Boolean {
        return a.x <= v.x && v.x < b.x
                && a.y <= v.y && v.y < b.y
    }

    fun fitIn(other: Borders): Borders = a.mixMax(other.a) to b.mixMin(other.b)

    fun any(predicate: (Vec2) -> Boolean): Boolean {
        val v = Vec2M()
        for (y in a.y until b.y) {
            for (x in a.x until b.x) {
                if (predicate(v.set(x, y))) return true
            }
        }
        return false
    }

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

