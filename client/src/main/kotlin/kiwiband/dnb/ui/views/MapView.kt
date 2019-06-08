package kiwiband.dnb.ui.views

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.manager.GameManager
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer

/**
 * View containing the map.
 */
class MapView(private val mgr: GameManager, width: Int, height: Int) : View(width, height) {

    private val center = Vec2(width / 2, height / 2)
    private val borders = Vec2(0, 0) to Vec2(width, height)
    private val mapBorders = Vec2(0, 0) to Vec2(mgr.getMap().width, mgr.getMap().height)
    private val offsets = center to (mapBorders.b - center)

    override fun draw(renderer: Renderer) {
        println("player: ${findPlayerPosition()} and ${findPlayerPosition().fitIn(offsets)}")
        println("center: $center")
        val offset = center - findPlayerPosition().fitIn(offsets)

        println("borders: $borders")
        println(offset)
        println(borders - offset)
        mgr.getMap().actors.forEachCell(borders - offset) { cell ->
            cell.firstOrNull()?.also {
                renderer.writeCharacter(it.getViewAppearance(), it.pos + offset)
            }
        }
    }

    private fun findPlayerPosition(): Vec2 {
        return mgr.getPlayer().pos
    }
}
