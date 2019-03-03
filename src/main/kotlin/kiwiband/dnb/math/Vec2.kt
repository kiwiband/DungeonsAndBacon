package kiwiband.dnb.math

class Vec2(x: Int, y: Int) : Vec2M(x, y) {
    constructor() : this(0, 0)

    constructor(v: Vec2M) : this(v.x, v.y)

    fun set(vx: Int, vy: Int): Vec2 {
        x = vx
        y = vy
        return this
    }

    fun set(v: Vec2M): Vec2 = set(v.x, v.y)

    fun add(v: Vec2M): Vec2  = set(x + v.x, y + v.y)

    fun sub(v: Vec2M): Vec2 = set(x - v.x, y - v.y)
}