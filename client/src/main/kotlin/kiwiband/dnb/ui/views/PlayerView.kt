package kiwiband.dnb.ui.views

import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.ASCIIART
import kiwiband.dnb.ui.Renderer

/**
 * View containing the player stats.
 */
class PlayerView(private val player: Player, width: Int, height: Int) : View(width, height) {
    override fun draw(renderer: Renderer) {

        renderer.writeText(ASCIIART.PLAYER, Vec2(7, 1))

        renderer.writeText("HERONAME", Vec2(3, 4))
        renderer.writeText("HP ${player.status.health}/${player.status.maxHealth}", Vec2(3, 5))
        renderer.writeText("LVL ${player.status.level}", Vec2(3, 6))
        renderer.writeText("EXP ${player.status.experience}/${player.status.maxExperience}", Vec2(3, 7))
        renderer.writeText("ATK ${player.status.getTotalAttack()}", Vec2(3, 8))
        renderer.writeText("DEF ${player.status.getTotalDefence()}", Vec2(3, 9))
    }
}
