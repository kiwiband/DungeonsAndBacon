package kiwiband.dnb.ui.views

import kiwiband.dnb.ui.Renderer

class Spacer(width: Int, height: Int) : View(width, height) {
    constructor() : this(0, 0)

    override fun draw(renderer: Renderer) {}

    override fun resize(width: Int, height: Int) {
        setSize(width, height)
    }
}