package kiwiband.dnb.ui.views

import kiwiband.dnb.ui.Renderer

abstract class View(val width: Int, val height: Int) {
    abstract fun draw(renderer: Renderer)
}
