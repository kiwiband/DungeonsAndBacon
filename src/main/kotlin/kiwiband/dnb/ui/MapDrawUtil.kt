package kiwiband.dnb.ui

import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.screen.Screen
import kiwiband.dnb.math.Vec2M

object MapDrawUtil {

    private const val BORDER_HORIZONTAL = '─'
    private const val BORDER_VERTICAL = '│'

    private const val CORNER_TOP_LEFT = '┌'
    private const val CORNER_TOP_RIGHT = '┐'
    private const val CORNER_BOTTOM_LEFT = '└'
    private const val CORNER_BOTTOM_RIGHT = '┘'

    fun writeCharacter(screen: Screen, character: Char, offset: Vec2M) {
        screen.setCharacter(
            offset.x,
            offset.y,
            TextCharacter(character, TextColor.ANSI.DEFAULT, TextColor.ANSI.DEFAULT)
        )
    }

    fun writeText(screen: Screen, text: String, offset: Vec2M) {
        text.forEachIndexed { i, c ->
            writeCharacter(screen, c, offset + Vec2M(i, 0))
        }
    }

    fun drawHorizontalLine(screen: Screen, startX: Int, endX: Int, y: Int) {
        for (x in startX..endX)
            screen.setCharacter(
                x,
                y,
                TextCharacter(
                    BORDER_HORIZONTAL,
                    TextColor.ANSI.DEFAULT,
                    TextColor.ANSI.DEFAULT
                )
            )
    }

    fun drawVerticalLine(screen: Screen, x: Int, startY: Int, endY: Int) {
        for (y in startY..endY)
            screen.setCharacter(
                x,
                y,
                TextCharacter(
                    BORDER_VERTICAL,
                    TextColor.ANSI.DEFAULT,
                    TextColor.ANSI.DEFAULT
                )
            )
    }

    fun drawTopLeftCorner(screen: Screen, position: Vec2M) {
        writeCharacter(screen, CORNER_TOP_LEFT, position)
    }

    fun drawTopRightCorner(screen: Screen, position: Vec2M) {
        writeCharacter(screen, CORNER_TOP_RIGHT, position)
    }

    fun drawBottomLeftCorner(screen: Screen, position: Vec2M) {
        writeCharacter(screen, CORNER_BOTTOM_LEFT, position)
    }

    fun drawBottomRightCorner(screen: Screen, position: Vec2M) {
        writeCharacter(screen, CORNER_BOTTOM_RIGHT, position)
    }
}
