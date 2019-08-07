package kiwiband.dnb.ui.views.layout.util

class Padding(val left: Int, val top: Int, val right: Int, val bottom: Int) {
    constructor(all: Int) : this(all, all, all, all)
    constructor() : this(0)

    fun vertical() = top + bottom
    fun horizontal() = left + right

    fun add(v: Int) = Padding(left + v, top + v, right + v, bottom + v)
}