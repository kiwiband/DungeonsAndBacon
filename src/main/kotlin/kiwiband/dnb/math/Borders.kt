package kiwiband.dnb.math

class Borders(val a: Vec2M, val b: Vec2M) {
    constructor(ax: Int, ay: Int, bx: Int, by: Int) : this(Vec2M(ax, ay), Vec2M(bx, by))

    operator fun contains(v: Vec2M): Boolean {
        return a.x <= v.x && v.x < b.x
                && a.y <= v.y && v.y < b.y
    }

    fun fitIn(other: Borders): Borders = a.mixMax(other.a) to b.mixMin(other.b)

    fun any(predicate: (Vec2M) -> Boolean): Boolean {
        val v = Vec2()
        for (y in a.y until b.y) {
            for (x in a.x until b.x) {
                if (predicate(v.set(x, y))) return true
            }
        }
        return false
    }

    fun forEach(consumer: (Vec2M) -> Unit) {
        val v = Vec2()
        for (y in a.y until b.y) {
            for (x in a.x until b.x) {
                consumer(v.set(x, y))
            }
        }
    }

    operator fun plus(offset: Vec2M) = Borders(a + offset, b + offset)
    operator fun minus(offset: Vec2M) = Borders(a - offset, b - offset)
}

