package kiwiband.dnb.ui.views

import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.Renderer

class LocalLoadMapView(width: Int, height: Int) : View(width, height) {

    override fun draw(renderer: Renderer) {
        renderer.writeText(UPPER_TEXT, Vec2M((width - UPPER_TEXT.length) / 2, height / 2 - 1))
        renderer.writeText(LOWER_TEXT, Vec2M((width - LOWER_TEXT.length) / 2, height / 2))
    }

    companion object {
        const val UPPER_TEXT = "Load an existing map?"
        const val LOWER_TEXT = "(y/n)"
    }
}
