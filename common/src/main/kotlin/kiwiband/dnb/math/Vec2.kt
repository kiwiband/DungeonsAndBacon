package kiwiband.dnb.math

import kiwiband.dnb.JSONSerializable
import org.json.JSONObject

/**
 * Immutable two-dimensional point.
 * @param x X coordinate
 * @param y Y coordinate
 */
@Suppress("unused")
open class Vec2(x: Int, y: Int) : JSONSerializable {
    open var x: Int = x
        protected set
    open var y: Int = y
        protected set

    constructor(v: Vec2) : this(v.x, v.y)
    constructor() : this(0, 0)

    fun isZero(): Boolean = x == 0 && y == 0

    operator fun unaryMinus() = Vec2(-x, -y)

    operator fun plus(v: Vec2) = Vec2(x + v.x, y + v.y)

    operator fun minus(v: Vec2) = Vec2(x - v.x, y - v.y)

    fun mixMax(v: Vec2): Vec2 = Vec2(Math.max(x, v.x), Math.max(y, v.y))

    fun mixMin(v: Vec2): Vec2 = Vec2(Math.min(x, v.x), Math.min(y, v.y))

    override fun equals(other: Any?): Boolean {
        if (other is Vec2) {
            return x == other.x && y == other.y
        }
        return super.equals(other)
    }


    fun distance(v: Vec2): Int = distance(v.x, v.y)

    fun distance(vx: Int, vy: Int): Int = Math.abs(vx - x) + Math.abs(vy - y)

    fun distanceEuler2(v: Vec2): Int = normEuler2(v.x - x, v.y - y)

    fun normalize(): Vec2 = Vec2(Integer.signum(x), Integer.signum(y))

    /**
     * Fits a point in borders.
     * @borders borders to fit in
     * @return fitted point
     */
    fun fitIn(borders: Borders) = Vec2(
        MyMath.clamp(x, borders.a.x, borders.b.x - 1),
        MyMath.clamp(y, borders.a.y, borders.b.y - 1)
    )

    /**
     * Fits a point in borders with included bottom right corner.
     * @borders borders to fit in
     * @return fitted point
     */
    fun fitInIncluded(borders: Borders) = Vec2(
        MyMath.clamp(x, borders.a.x, borders.b.x),
        MyMath.clamp(y, borders.a.y, borders.b.y)
    )


    infix fun to(that: Vec2): Borders = Borders(this, that)

    override fun hashCode(): Int = 0xffff * x + y

    override fun toJSON(): JSONObject {
        return JSONObject().put("x", x).put("y", y)
    }

    override fun toString(): String {
        return "$x, $y"
    }

    companion object {
        fun normEuler2(x: Int, y: Int): Int {
            return x * x + y * y
        }
    }
}
