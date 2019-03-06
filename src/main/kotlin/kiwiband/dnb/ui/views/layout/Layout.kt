package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.views.View

data class ChildView(val offset: Vec2M, val view: View)

abstract class Layout(width: Int, height: Int) : View(width, height) {
    protected val children = mutableListOf<ChildView>()
}