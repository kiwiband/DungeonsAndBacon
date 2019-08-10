package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View

class Spacer(width: Int, height: Int) : View(width, height) {
    constructor() : this(0, 0)

    override fun draw(renderer: Renderer) {}

    override fun resize(width: Int, height: Int) {
        setSize(width, height)
    }
}