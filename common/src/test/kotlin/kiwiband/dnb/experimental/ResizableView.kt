package kiwiband.dnb.experimental

import kiwiband.dnb.math.Vec2M

import kiwiband.dnb.math.Vec2

fun Vec2.str(): String = "($x, $y)"
class View(val posX: Vec2M, val posY: Vec2M, val sizeX: Vec2M, val sizeY: Vec2M, val char: Char) {

    val views = mutableListOf<View>()

    fun render(parentPos: Vec2, parentSize: Vec2, screen: Array<CharArray>) {
        val s = getMinSize()
        val thisPos = Vec2(getAbsPos(posX, parentSize.x), getAbsPos(posY, parentSize.y))
        val thisSize = Vec2(getAbsSize(sizeX, parentSize.x, s.x), getAbsSize(sizeY, parentSize.y, s.y))

        println("p: ${thisPos.str()} s: ${thisSize.str()}, when ${sizeX.str()}")
        val pos = thisPos + parentPos
        for (i in 0 until thisSize.x) {
            for (j in 0 until thisSize.y) {
                screen[pos.y + j][pos.x + i] = char
            }
        }

        for (v in views) {
            v.render(thisPos, thisSize, screen)
        }
    }

    fun getMinSize(): Vec2 {
        val r = Vec2M(sizeX.x, sizeY.x)
        for (v in views) {
            r.setMax(Vec2(v.posX.x, v.posY.x) + v.getMinSize())
        }
        return r
    }

    private fun getAbsPos(pos: Vec2, pSize: Int) = if (pos.x == 0) pos.y * pSize / 100 else pos.x
    private fun getAbsSize(size: Vec2, pSize: Int, default: Int): Int {
        if (size.y != 0)
            return Math.max(size.y * pSize / 100, size.x)
        if (size.x != 0)
            return size.x
        return default
    }
}

fun Vec2M.setMax(v: Vec2): Vec2M = set(Math.max(x, v.x), Math.max(y, v.y))

fun main() {
    val rootView = View(Vec2M(), Vec2M(), Vec2M(80, 0), Vec2M(24, 0), '.')
    val someView = View(Vec2M(), Vec2M(), Vec2M(10, 33), Vec2M(10, 100), '#')
    val rbox = View(Vec2M(0, 33), Vec2M(), Vec2M(0, 68), Vec2M(0, 0), '|')

    val text = View(Vec2M(), Vec2M(), Vec2M(10, 0), Vec2M(1, 0), 't')
    val text2 = View(Vec2M(), Vec2M(1, 0), Vec2M(15, 0), Vec2M(3, 0), 'g')
    val text3 = View(Vec2M(0, 50), Vec2M(0, 50), Vec2M(4, 0), Vec2M(4, 0), 'a')

    rbox.views.add(text)
    rbox.views.add(text2)
    rbox.views.add(text3)

    rootView.views.add(someView)
    rootView.views.add(rbox)
    val h = 30
    val w = 300
    val screen = Array(h) { CharArray(w) { ' ' } }
    rootView.render(Vec2M(), Vec2M(), screen)

    for (i in 0 until h) {
        for (j in 0 until w) {
            print(screen[i][j])
        }
        println()
    }
}