package kiwiband.dnb.ui.views

import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.Renderer

class PlayerView(width: Int, height: Int) : View(width, height) {
    override fun draw(renderer: Renderer) {
        renderer.writeText("L(O_o)-(===>", Vec2M(7, 1))

        renderer.writeText("HERONAME", Vec2M(3, 4))
        renderer.writeText("HP 15/15", Vec2M(3, 5))
        renderer.writeText("LVL 1", Vec2M(3, 6))
        renderer.writeText("EXP 1/10", Vec2M(3, 7))
        renderer.writeText("ATK 4", Vec2M(3, 8))
        renderer.writeText("DEF 1", Vec2M(3, 9))
    }
}
