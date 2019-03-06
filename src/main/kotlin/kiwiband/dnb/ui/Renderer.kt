package kiwiband.dnb.ui

import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.screen.Screen
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.math.Vec2M

class Renderer(private val screen: Screen, val offset: Vec2) {
    
    fun writeCharacter(character: Char, internalOffset: Vec2M) {
        val resultOffset = offset + internalOffset
        screen.setCharacter(
            resultOffset.x,
            resultOffset.y,
            TextCharacter(character, TextColor.ANSI.DEFAULT, TextColor.ANSI.DEFAULT)
        )
    }

    fun writeText(text: String, offset: Vec2M) {
        text.forEachIndexed { i, c ->
            writeCharacter(c, offset + Vec2M(i, 0))
        }
    }

    fun drawBox(width: Int, height: Int) {
        drawHorizontalLine(1, width, 0)
        drawHorizontalLine(1, width, height + 1)
        drawVerticalLine(0, 1, height)
        drawVerticalLine(width + 1, 1, height)

        drawTopLeftCorner(Vec2M(0, 0))
        drawTopRightCorner(Vec2M(width + 1, 0))
        drawBottomLeftCorner(Vec2M(0, height + 1))
        drawBottomRightCorner(Vec2M(width + 1, height + 1))
    }


    fun drawHorizontalLine(startX: Int, endX: Int, y: Int) {
        for (x in startX..endX)
            writeCharacter(BORDER_HORIZONTAL, Vec2M(x, y))
    }

    fun drawVerticalLine(x: Int, startY: Int, endY: Int) {
        for (y in startY..endY)
            writeCharacter(BORDER_VERTICAL, Vec2M(x, y))
    }

    fun drawTopLeftCorner(position: Vec2M) {
        writeCharacter(CORNER_TOP_LEFT, position)
    }

    fun drawTopRightCorner(position: Vec2M) {
        writeCharacter(CORNER_TOP_RIGHT, position)
    }

    fun drawBottomLeftCorner(position: Vec2M) {
        writeCharacter(CORNER_BOTTOM_LEFT, position)
    }

    fun drawBottomRightCorner(position: Vec2M) {
        writeCharacter(CORNER_BOTTOM_RIGHT, position)
    }

    
    companion object {
        private const val BORDER_HORIZONTAL = '─'
        private const val BORDER_VERTICAL = '│'

        private const val CORNER_TOP_LEFT = '┌'
        private const val CORNER_TOP_RIGHT = '┐'
        private const val CORNER_BOTTOM_LEFT = '└'
        private const val CORNER_BOTTOM_RIGHT = '┘'
    }
}