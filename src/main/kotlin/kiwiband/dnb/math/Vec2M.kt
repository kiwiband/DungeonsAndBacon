package kiwiband.dnb.math

open class Vec2M(var x: Int, var y: Int) {
    constructor(v: Vec2M) : this(v.x, v.y)

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

    override fun hashCode(): Int = 31 * x + y
}
