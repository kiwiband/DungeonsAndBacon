package kiwiband.dnb.ui.views

import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.math.contains
import kiwiband.dnb.ui.Renderer

class MapView(private val map: LocalMap, width: Int, height: Int) : View(width, height) {

    private val center = Vec2M(width / 2, height / 2)
    private val borders = Vec2M(0, 0) to Vec2M(width, height)

    override fun draw(renderer: Renderer) {
        val playerPosition = findPlayerPosition()

        val mapOffset = Vec2M(
            Math.min(Math.max(playerPosition.x, width / 2), map.width - width / 2 + 1),
            Math.min(Math.max(playerPosition.y, height / 2), map.height - height / 2 + 1)
        )

        map.actors.forEach {
            val position = it.position - mapOffset + center
            if (position in borders)
                renderer.writeCharacter(it.getViewAppearance(), position)
        }
    }

    private fun findPlayerPosition(): Vec2M {
        return map.actors.find { it is Player }?.position ?: Vec2M(width / 2, height / 2)
    }
}