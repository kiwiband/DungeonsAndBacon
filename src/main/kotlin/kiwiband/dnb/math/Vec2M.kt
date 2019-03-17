package kiwiband.dnb.math

open class Vec2M(var x: Int, var y: Int) {
    constructor(v: Vec2M) : this(v.x, v.y)
    constructor(v: Int) : this(v, v)
    constructor() : this(0)

    fun isZero() : Boolean = x == 0 && y == 0

    operator fun unaryMinus() = Vec2M(-x, -y)

    operator fun plus(v: Vec2M) = Vec2M(x + v.x, y + v.y)

    operator fun minus(v: Vec2M) = Vec2M(x - v.x, y - v.y)

    fun mixMax(v: Vec2M): Vec2M = Vec2M(Math.max(x, v.x), Math.max(y, v.y))

    fun mixMin(v: Vec2M): Vec2M = Vec2M(Math.min(x, v.x), Math.min(y, v.y))

    override fun equals(other: Any?): Boolean {
        if (other is Vec2M) {
            return x == other.x && y == other.y
        }
        return super.equals(other)
    }

    fun distance(v: Vec2M): Int = distance(v.x, v.y)

    fun distance(vx: Int, vy: Int): Int = Math.abs(vx - x) + Math.abs(vy - y)

    fun normalize(): Vec2M = Vec2M(normalize(x), normalize(y))

    fun fitIn(borders: Borders) = Vec2M(
        MyMath.clamp(x, borders.a.x, borders.b.x),
        MyMath.clamp(y, borders.a.y, borders.b.y)
    )

    infix fun to(that: Vec2M): Borders = Borders(this, that)

    override fun hashCode(): Int = 31 * x + y

    companion object {
        private fun normalize(x: Int) = if (x < 0) -1 else if (x > 0) 1 else 0
    }
}
