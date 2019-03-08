package kiwiband.dnb.ui.views

import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.Renderer

class MapView(private val map: LocalMap, width: Int, height: Int) : View(width, height) {

    private val center = Vec2M(width / 2, height / 2)
    private val borders = Vec2M(0, 0) to Vec2M(width, height)
    private val mapBorders = Vec2M(0, 0) to Vec2M(map.width, map.height)
    private val offsets = center to (mapBorders.b - center)

    override fun draw(renderer: Renderer) {
        val offset = center - findPlayerPosition().fitIn(offsets)

        map.actors.forEachCell(borders - offset) { cell ->
            cell.firstOrNull()?.also {
                renderer.writeCharacter(it.getViewAppearance(), it.pos + offset)
            }
        }
    }

    private fun findPlayerPosition(): Vec2M {
        return map.player?.pos ?: center
    }
}
