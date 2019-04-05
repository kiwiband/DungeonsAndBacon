package kiwiband.dnb.ui.views

import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer

class MapView(private val map: LocalMap, width: Int, height: Int) : View(width, height) {

    private val center = Vec2(width / 2, height / 2)
    private val borders = Vec2(0, 0) to Vec2(width, height)
    private val mapBorders = Vec2(0, 0) to Vec2(map.width, map.height)
    private val offsets = center to (mapBorders.b - center)

    override fun draw(renderer: Renderer) {
        val offset = center - findPlayerPosition().fitIn(offsets)

        map.actors.forEachCell(borders - offset) { cell ->
            cell.firstOrNull()?.also {
                renderer.writeCharacter(it.getViewAppearance(), it.pos + offset)
            }
        }
    }

    private fun findPlayerPosition(): Vec2 {
        return map.player?.pos ?: center
    }
}
