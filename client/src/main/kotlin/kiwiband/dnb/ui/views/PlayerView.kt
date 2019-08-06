package kiwiband.dnb.ui.views

import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ASCIIART
import kiwiband.dnb.manager.GameManager
import kiwiband.dnb.ui.Renderer

/**
 * View containing the player stats.
 */
class PlayerView(private val mgr: GameManager, width: Int, height: Int) : View(width, height) {
    override fun draw(renderer: Renderer) {
        val player = mgr.getPlayer()

        renderer.writeText(ASCIIART.PLAYER, Vec2(7, 1))

        renderer.writeText("HERONAME ${(player.playerId) % 10}", Vec2(3, 4))
        Drawer.drawCreatureStatus(renderer, player.status, 4, 5)
    }
}
