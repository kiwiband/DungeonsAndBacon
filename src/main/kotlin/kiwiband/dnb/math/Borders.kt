package kiwiband.dnb.math

typealias Borders = Pair<Vec2M, Vec2M>

fun borders(x1: Int, y1: Int, x2: Int, y2: Int) = Vec2M(x1, y1) to Vec2M(x2, y2)

operator fun Borders.contains(v: Vec2M): Boolean {
    return first.x <= v.x && v.x < second.x
            && first.y <= v.y && v.y < second.y
}

fun Borders.fitIn(b: Borders): Borders = first.mixMax(b.first) to second.mixMin(b.second)


fun Borders.any(predicate: (Vec2M) -> Boolean): Boolean {
    val v = Vec2()
    for (y in first.y until second.y) {
        for (x in first.x until second.x) {
            if (predicate(v.set(x, y))) return true
        }
    }
    return false
}
