package kiwiband.dnb.ui.views

import kiwiband.dnb.map.LocalMap

class MapView(private val map: LocalMap, width: Int, height: Int) : View(width, height) {

    override fun to2DArray(): Array<CharArray> {
        val result = Array(height) { CharArray(width) { ' ' } }

        map.actors.forEach {
            val x = it.position.x
            val y = it.position.y
            if (x < width && y < height)
                result[y][x] = it.getViewAppearance()
        }

        result[height - 1][width - 1] = '#'
        return result
    }
}