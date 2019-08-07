package kiwiband.dnb.ui

import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.screen.Screen
import kiwiband.dnb.actors.ViewAppearance
import kiwiband.dnb.math.Borders
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.math.Vec2

/**
 * Class for rendering characters on the screen.
 * Contains the drawing offset and some drawing primitives.
 * @param screen screen to render on
 */
class Renderer(var screen: Screen) {
    /**
     * Drawing offset of this renderer
     */
    val offset: Vec2M = Vec2M()

    private var borders = Vec2() to Vec2(Int.MAX_VALUE, Int.MAX_VALUE)

    private val defaultAppearance = ViewAppearance('.')

    /**
     * Sets the absolute offset of the renderer.
     * @param x offset column on the screen
     * @param y offset row on the screen
     */
    fun setOffset(x: Int, y: Int) {
        offset.set(x, y)
    }

    /**
     * Executes the function, returning the offset to previous state after execution.
     * @param f function to execute
     */
    fun withOffset(f: () -> Unit) {
        val previousOffset = Vec2(offset)
        f()
        offset.set(previousOffset)
    }

    fun withOffsetLimited(width: Int, height: Int, f: () -> Unit) {
        withOffsetLimited(Vec2() to Vec2(width, height), f)
    }

    fun withOffsetLimited(borders: Borders, f: () -> Unit) {
        val previousBorders = borders
        this.borders = borders + offset
        withOffset(f)
        this.borders = previousBorders
    }

    /**
     * Sets a character on a screen relative to current offset.
     * @param character character to set
     * @param internalOffset character position relative to offset
     */
    fun writeCharacter(character: Char, internalOffset: Vec2) {
        defaultAppearance.char = character
        writeCharacter(defaultAppearance, internalOffset)
    }

    fun writeCharacter(appearance: ViewAppearance, internalOffset: Vec2) {
        val resultOffset = offset + internalOffset
        if (resultOffset in borders) {
            screen.setCharacter(
                resultOffset.x,
                resultOffset.y,
                TextCharacter(appearance.char, appearance.color, appearance.bgColor)
            )
        }
    }

    /**
     * Writes a text string horizontally, starting on a given coordinate.
     * @param text string to write
     * @param offset relative position
     */
    fun writeText(text: String, offset: Vec2) {
        text.forEachIndexed { i, c ->
            writeCharacter(c, offset + Vec2(i, 0))
        }
    }

    fun writeMultiLineText(text: String) {
        text.lines().forEachIndexed { i, line ->
            writeText(line, Vec2(0, i))
        }
    }

    /**
     * Draws a box from a top left corner.
     * @param width box width
     * @param height box height
     */
    fun drawBox(width: Int, height: Int) {
        drawHorizontalLine(1, width - 1, 0)
        drawHorizontalLine(1, width - 1, height - 1)
        drawVerticalLine(0, 1, height - 1)
        drawVerticalLine(width - 1, 1, height - 1)

        drawTopLeftCorner(Vec2(0, 0))
        drawTopRightCorner(Vec2(width - 1, 0))
        drawBottomLeftCorner(Vec2(0, height - 1))
        drawBottomRightCorner(Vec2(width - 1, height - 1))
    }

    /**
     * Draws a horizontal line relative to the offset.
     * @param startX start X coordinate
     * @param endX end X coordinate
     * @param y Y coordinate
     */
    fun drawHorizontalLine(startX: Int, endX: Int, y: Int) {
        for (x in startX..endX)
            writeCharacter(BORDER_HORIZONTAL, Vec2(x, y))
    }

    /**
     * Draws a vertical line relative to the offset.
     * @param x X coordinate
     * @param startY start Y coordinate
     * @param endY end Y coordinate
     */
    fun drawVerticalLine(x: Int, startY: Int, endY: Int) {
        for (y in startY..endY)
            writeCharacter(BORDER_VERTICAL, Vec2(x, y))
    }

    /**
     * Draws a top left corner relative to the offset.
     * @param position position to draw
     */
    fun drawTopLeftCorner(position: Vec2) {
        writeCharacter(CORNER_TOP_LEFT, position)
    }

    /**
     * Draws a top right corner relative to the offset.
     * @param position position to draw
     */
    fun drawTopRightCorner(position: Vec2) {
        writeCharacter(CORNER_TOP_RIGHT, position)
    }

    /**
     * Draws a bottom left corner relative to the offset.
     * @param position position to draw
     */
    fun drawBottomLeftCorner(position: Vec2) {
        writeCharacter(CORNER_BOTTOM_LEFT, position)
    }

    /**
     * Draws a bottom right corner relative to the offset.
     * @param position position to draw
     */
    fun drawBottomRightCorner(position: Vec2) {
        writeCharacter(CORNER_BOTTOM_RIGHT, position)
    }

    /**
     * Clears the entire screen, regardless of offset.
     */
    fun clearScreen() {
        screen.clear()
    }

    /**
     * Refreshes the screen.
     */
    fun refreshScreen() {
        screen.refresh()
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
