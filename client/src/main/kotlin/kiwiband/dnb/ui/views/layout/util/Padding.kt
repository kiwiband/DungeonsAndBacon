package kiwiband.dnb.ui.views.layout.util

class Padding(val left: Int = 0, val top: Int = 0, val right: Int = 0, val bottom: Int = 0) {
    constructor(all: Int) : this(all, all, all, all)
    constructor() : this(0)

    fun vertical() = top + bottom
    fun horizontal() = left + right

    fun add(v: Int) = Padding(left + v, top + v, right + v, bottom + v)
}