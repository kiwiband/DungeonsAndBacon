package kiwiband.dnb.ui.views

import com.googlecode.lanterna.screen.Screen
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.MapDrawUtil.drawBottomLeftCorner
import kiwiband.dnb.ui.MapDrawUtil.drawBottomRightCorner
import kiwiband.dnb.ui.MapDrawUtil.drawHorizontalLine
import kiwiband.dnb.ui.MapDrawUtil.drawTopLeftCorner
import kiwiband.dnb.ui.MapDrawUtil.drawTopRightCorner
import kiwiband.dnb.ui.MapDrawUtil.drawVerticalLine

class SequenceLayout(width: Int, height: Int, private val isHorizontal: Boolean = true) : Layout(width, height) {
    fun addChild(view: View) {
        children.add(ChildView(Vec2M(0, 0), view))
    }

    override fun draw(screen: Screen, offset: Vec2M) {
        val sequenceOffset = Vec2(0, 0)

        val boxSequenceStep = if (isHorizontal) Vec2M(2, 0) else Vec2M(0, 2)

        for (child in children) {
            val view = child.view
            if (view !is Layout) {
                drawBox(screen, offset + sequenceOffset, view.width, view.height)
                view.draw(screen, offset + sequenceOffset + Vec2M(1, 1))
                sequenceOffset.add(boxSequenceStep)
            } else {
                view.draw(screen, offset + sequenceOffset)
            }
            if (isHorizontal)
                sequenceOffset.add(Vec2M(view.width, 0))
            else
                sequenceOffset.add(Vec2M(0, view.height))
        }
    }

    private fun drawBox(screen: Screen, start: Vec2M, boxWidth: Int, boxHeight: Int) {
        val end = Vec2(start)
        end.add(Vec2M(boxWidth, boxHeight))

        drawHorizontalLine(screen, start.x + 1, end.x, start.y)
        drawHorizontalLine(screen, start.x + 1, end.x, end.y + 1)
        drawVerticalLine(screen, start.x, start.y + 1, end.y)
        drawVerticalLine(screen, end.x + 1, start.y + 1, end.y)

        end.add(Vec2M(1, 1))

        drawTopLeftCorner(screen, Vec2M(start.x, start.y))
        drawTopRightCorner(screen, Vec2M(end.x, start.y))
        drawBottomLeftCorner(screen, Vec2M(start.x, end.y))
        drawBottomRightCorner(screen, Vec2M(end.x, end.y))
    }


}