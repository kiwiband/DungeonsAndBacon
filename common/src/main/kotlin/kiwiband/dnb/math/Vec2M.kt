package kiwiband.dnb.math

/**
 * Mutable two-dimensional point.
 */
@Suppress("UNUSED")
class Vec2M(x: Int, y: Int) : Vec2() {
    override var x: Int = x
        public set
    override var y: Int = y
        public set

    constructor() : this(0, 0)

    constructor(v: Vec2) : this(v.x, v.y)

    fun set(vx: Int, vy: Int): Vec2M {
        x = vx
        y = vy
        return this
    }

    fun set(v: Vec2): Vec2M = set(v.x, v.y)

    fun add(v: Vec2): Vec2M = set(x + v.x, y + v.y)

    fun add(x: Int, y: Int): Vec2M = set(this.x + x, this.y + y)

    fun sub(v: Vec2): Vec2M = set(x - v.x, y - v.y)
}
