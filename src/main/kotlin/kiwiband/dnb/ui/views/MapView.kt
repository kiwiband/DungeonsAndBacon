package kiwiband.dnb.ui.views

import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.ui.MapDrawUtil.writeText

class MapView(private val map: LocalMap, width: Int, height: Int) : View(width, height) {

    override fun to2DArray(): Array<CharArray> {
        val result = Array(height) { CharArray(width) { ' ' } }

        map.actors.forEach {
            val x = it.position.x
            val y = it.position.y
            if (x < width && y < height)
                result[y][x] = it.getViewAppearance()
        }

        writeText(result, "THIS IS A MAP", 18, 10)
        return result
    }
}