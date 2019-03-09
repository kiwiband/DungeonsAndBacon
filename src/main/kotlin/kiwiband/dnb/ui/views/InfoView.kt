package kiwiband.dnb.ui.views

import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.Renderer

class InfoView(width: Int, height: Int) : View(width, height) {
    override fun draw(renderer: Renderer) {
        renderer.writeText("DUNGEONS", Vec2M(10, 4))
        renderer.writeText("AND", Vec2M(12, 5))
        renderer.writeText("BACON", Vec2M(11, 6))
    }
}
