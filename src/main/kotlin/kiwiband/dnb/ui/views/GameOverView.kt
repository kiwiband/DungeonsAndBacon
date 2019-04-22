package kiwiband.dnb.ui.views

import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer

/**
 * View with 'game over' message.
 */
class GameOverView(width: Int, height: Int) : View(width, height) {

    override fun draw(renderer: Renderer) {
        renderer.writeText(TEXT, Vec2((width - TEXT.length) / 2, height / 2))
    }

    companion object {
        const val TEXT = "GAME OVER :("
    }
}
