package kiwiband.dnb.math

open class Vec2M(var x: Int, var y: Int) {
    constructor(v: Vec2M) : this(v.x, v.y)

    operator fun unaryMinus() = Vec2M(-x, -y)

    operator fun plus(v: Vec2M) = Vec2M(x + v.x, y + v.y)

    operator fun minus(v: Vec2M) = Vec2M(x - v.x, y - v.y)
}