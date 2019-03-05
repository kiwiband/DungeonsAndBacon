package kiwiband.dnb.ui.views

import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2M

class MapView(private val map: LocalMap, width: Int, height: Int) : View(width, height) {

    override fun to2DArray(): Array<CharArray> {
        val result = Array(height) { CharArray(width) { ' ' } }

        val playerPosition = findPlayerPosition()
        val offsetX = playerPosition.x
        val offsetY = playerPosition.y

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