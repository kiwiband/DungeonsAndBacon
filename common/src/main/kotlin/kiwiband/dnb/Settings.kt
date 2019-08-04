package kiwiband.dnb

import java.lang.NumberFormatException


object Settings {
    var host = Default.HOST
    var port = Default.PORT

    var mapWidth = Default.MAP_WIDTH
    var mapHeight = Default.MAP_HEIGHT

    var mobsCount = Default.MOBS_COUNT

    var fovRadius = Default.FOV_RADIUS

    object Default {
        const val HOST = "localhost"
        const val PORT = 12345
        const val MAP_WIDTH = 80

        const val MAP_HEIGHT = 24
        const val MOBS_COUNT = 50

        const val FOV_RADIUS = 10
    }

    @JvmStatic
    fun getIntFromMap(map: Map<String, String>, element: String): Int? {
        return map[element]?.let {
            try {
                Integer.parseInt(it)
            } catch (e: NumberFormatException) {
                println("Wrong format of \"$element\"")
                null
            }
        }
    }
}