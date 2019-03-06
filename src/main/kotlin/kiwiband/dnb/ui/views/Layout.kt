package kiwiband.dnb.ui.views

import com.googlecode.lanterna.screen.Screen
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.MapDrawUtil.drawBottomLeftCorner
import kiwiband.dnb.ui.MapDrawUtil.drawBottomRightCorner
import kiwiband.dnb.ui.MapDrawUtil.drawHorizontalLine
import kiwiband.dnb.ui.MapDrawUtil.drawTopLeftCorner
import kiwiband.dnb.ui.MapDrawUtil.drawTopRightCorner
import kiwiband.dnb.ui.MapDrawUtil.drawVerticalLine

data class ChildView(val offset: Vec2M, val view: View)

abstract class Layout(width: Int, height: Int) : View(width, height) {

    protected val children = mutableListOf<ChildView>()

    protected fun addChild(offset: Vec2M, view: View) {
        children.add(ChildView(offset, view))
    }
}