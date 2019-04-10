package kiwiband.dnb.ui.views

import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer

/**
 * View containing the player stats.
 */
class PlayerView(width: Int, height: Int) : View(width, height) {
    override fun draw(renderer: Renderer) {
        renderer.writeText("L(O_o)-(===>", Vec2(7, 1))

        renderer.writeText("HERONAME", Vec2(3, 4))
        renderer.writeText("HP 15/15", Vec2(3, 5))
        renderer.writeText("LVL 1", Vec2(3, 6))
        renderer.writeText("EXP 1/10", Vec2(3, 7))
        renderer.writeText("ATK 4", Vec2(3, 8))
        renderer.writeText("DEF 1", Vec2(3, 9))
    }
}
