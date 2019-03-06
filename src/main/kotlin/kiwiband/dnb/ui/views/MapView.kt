package kiwiband.dnb.ui.views

import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2M

class MapView(private val map: LocalMap, width: Int, height: Int) : View(width, height) {

    override fun to2DArray(): Array<CharArray> {
        val result = Array(height) { CharArray(width) { ' ' } }

        val playerPosition = findPlayerPosition()
        val offsetX = Math.min(Math.max(playerPosition.x, width / 2), map.x - width / 2 + 1)
        val offsetY = Math.min(Math.max(playerPosition.y, height / 2), map.y - height / 2 + 1)

        map.actors.forEach {
            val x = it.position.x - offsetX + width / 2
            val y = it.position.y - offsetY + height / 2
            if (x >= 0 && y >= 0 && x < width && y < height)
                result[y][x] = it.getViewAppearance()
        }

        return result
    }

    private fun findPlayerPosition(): Vec2M {
        return map.actors.find { it is Player }?.position ?: Vec2M(width / 2, height / 2)
    }
}