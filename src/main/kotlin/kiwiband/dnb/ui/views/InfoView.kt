package kiwiband.dnb.ui.views

import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer

class InfoView(width: Int, height: Int) : View(width, height) {
    override fun draw(renderer: Renderer) {
        renderer.writeText("DUNGEONS", Vec2(10, 4))
        renderer.writeText("AND", Vec2(12, 5))
        renderer.writeText("BACON", Vec2(11, 6))
    }
}
