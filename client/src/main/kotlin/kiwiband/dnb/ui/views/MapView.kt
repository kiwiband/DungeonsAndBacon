package kiwiband.dnb.ui.views

import kiwiband.dnb.manager.GameManager
import kiwiband.dnb.math.Borders
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer

/**
 * View containing the map.
 */
class MapView(private val mgr: GameManager, width: Int, height: Int) : View(width, height) {

    private val center = Vec2(width / 2, height / 2)
    private val borders = Vec2(0, 0) to Vec2(width, height)

    override fun draw(renderer: Renderer) {
        val offsets = getOffsets(getMapBorders())
        val offset = center - findPlayerPosition().fitIn(offsets)

        mgr.getMap().actors.forEachCell(borders - offset) { cell ->
            cell.firstOrNull()?.also {
                renderer.writeCharacter(it.getViewAppearance(), it.pos + offset)
            }
        }
    }

    private fun getMapBorders() = mgr.getMap().let { Vec2(0, 0) to Vec2(it.width, it.height) }

    private fun getOffsets(mapBorders: Borders): Borders = center to (mapBorders.b - center).mixMax(center + Vec2(1, 1))

    private fun findPlayerPosition(): Vec2 {
        return mgr.getPlayer().pos
    }
}
