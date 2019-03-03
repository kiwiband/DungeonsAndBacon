package kiwiband.dnb.math

open class Vec2M(var x: Int, var y: Int) {
    constructor(v: Vec2M) : this(v.x, v.y)

    operator fun unaryMinus() = Vec2M(-x, -y)

    operator fun plus(v: Vec2M) = Vec2M(x + v.x, y + v.y)

    operator fun minus(v: Vec2M) = Vec2M(x - v.x, y - v.y)

    override fun equals(other: Any?): Boolean {
        if (other is Vec2M) {
            return x == other.x && y == other.y
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}

operator fun Pair<Vec2M, Vec2M>.contains(v: Vec2M): Boolean {
    return first.x <= v.x && v.x <= second.x
            && first.y <= v.y && v.y <= second.y
}