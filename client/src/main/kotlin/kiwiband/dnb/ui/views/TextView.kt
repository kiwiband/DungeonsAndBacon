package kiwiband.dnb.ui.views

import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.TextAlignment.*

class TextView(text: String, private val alignment: TextAlignment) : View(calcSize(text)) {
    private val lines = text.lines()

    constructor(text: String) : this(text, CENTER)

    override fun draw(renderer: Renderer) {
        renderer.withOffset {
            lines.forEachIndexed { i, line ->
                val offset = when (alignment) {
                    LEFT -> Vec2(0, i)
                    CENTER -> Vec2((width - line.length) / 2, i)
                    RIGHT -> Vec2(width - line.length, i)
                }
                renderer.writeText(line, offset)
            }
        }
    }

    override fun resize(width: Int, height: Int) = setSize(width, height)

    companion object {
        private fun calcSize(text: String): Vec2 {
            val lines = text.lines()
            val maxLen = lines.maxBy { it.length }?.length ?: 0
            return Vec2(maxLen, lines.size)
        }
    }
}

enum class TextAlignment {
    LEFT, CENTER, RIGHT
}