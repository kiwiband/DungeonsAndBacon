package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.views.View

data class ChildView(val offset: Vec2, val view: View)

abstract class Layout(width: Int, height: Int) : View(width, height) {
    protected val children = mutableListOf<ChildView>()

    fun clear() {
        children.clear()
    }
}
