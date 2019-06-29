package kiwiband.dnb.ui.views

import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer

/**
 * Dialog view for loading the map.
 */
class MultiplayerLoadMapView(width: Int, height: Int) : View(width, height) {

    override fun draw(renderer: Renderer) {
        renderer.writeText(UPPER_TEXT, Vec2((width - UPPER_TEXT.length) / 2, height / 2))

    }

    companion object {
        const val UPPER_TEXT = "Loading map..."
    }
}
