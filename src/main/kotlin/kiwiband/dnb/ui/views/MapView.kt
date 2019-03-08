package kiwiband.dnb.ui.views

import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.math.contains
import kiwiband.dnb.ui.Renderer

class MapView(private val map: LocalMap, width: Int, height: Int) : View(width, height) {

    private val center = Vec2M(width / 2, height / 2)
    private val borders = Vec2M(0, 0) to Vec2M(width, height)

    override fun draw(renderer: Renderer) {
        val playerPosition = findPlayerPosition()

        val mapOffset = Vec2M(
            Math.min(Math.max(playerPosition.x, width / 2), map.width - width / 2),
            Math.min(Math.max(playerPosition.y, height / 2), map.height - height / 2)
        )

        val pos = Vec2()
        map.backgroundActors.forEach {
            pos.set(it.pos).sub(mapOffset).add(center)
            if (pos in borders)
                renderer.writeCharacter(it.getViewAppearance(), pos)
        }
        map.actors.forEach {
            pos.set(it.pos).sub(mapOffset).add(center)
            if (pos in borders)
                renderer.writeCharacter(it.getViewAppearance(), pos)
        }
    }

    private fun findPlayerPosition(): Vec2M {
        return map.actors.find { it is Player }?.pos ?: Vec2M(width / 2, height / 2)
    }
}
