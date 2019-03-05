package kiwiband.dnb.ui

object MapDrawUtil {

    private const val BORDER_HORIZONTAL = '─'
    private const val BORDER_VERTICAL = '│'

    private const val CORNER_TOP_LEFT = '┌'
    private const val CORNER_TOP_RIGHT = '┐'
    private const val CORNER_BOTTOM_LEFT = '└'
    private const val CORNER_BOTTOM_RIGHT = '┘'

    private const val T_UP = '┴'
    private const val T_DOWN = '┬'
    private const val T_LEFT = '┤'
    private const val T_RIGHT = '├'

    private const val CENTER_X = 49
    private const val CENTER_Y = 11

    private fun drawHorizontalLine(scene: Array<CharArray>, startX: Int, endX: Int, y: Int) {
        for (x in startX..endX)
            scene[y][x] = BORDER_HORIZONTAL
    }
    
    private fun drawVerticalLine(scene: Array<CharArray>, x: Int, startY: Int, endY: Int) {
        for (y in startY..endY)
            scene[y][x] = BORDER_VERTICAL
    }

    fun addBorders(scene: Array<CharArray>) {
        val width = scene[0].size - 1
        val height = scene.size - 1
        
        drawHorizontalLine(scene, 0, width, 0)
        drawHorizontalLine(scene, 0, width, height)
        drawVerticalLine(scene, 0, 0, height)
        drawVerticalLine(scene, width, 0, height)

        drawVerticalLine(scene, CENTER_X, 1, height - 1)
        drawHorizontalLine(scene, CENTER_X + 1, width - 1, CENTER_Y)

        scene[0][0] = CORNER_TOP_LEFT 
        scene[0][width] = CORNER_TOP_RIGHT
        scene[height][0] = CORNER_BOTTOM_LEFT
        scene[height][width] = CORNER_BOTTOM_RIGHT

        scene[0][CENTER_X] = T_DOWN
        scene[height][CENTER_X] = T_UP
        scene[CENTER_Y][width] = T_LEFT
        scene[CENTER_Y][CENTER_X] = T_RIGHT
    }
}