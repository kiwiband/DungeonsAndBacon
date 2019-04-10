package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.views.View

/**
 * Data class containing the information about the child views' positions.
 * @param offset relative position
 * @param view view
 */
data class ChildView(val offset: Vec2, val view: View)

/**
 * Base class for layouts - container views.
 */
abstract class Layout(width: Int, height: Int) : View(width, height) {
    /**
     * List of all views in layout
     */
    protected val children = mutableListOf<ChildView>()

    /**
     * Removes all the children from the layout.
     */
    fun clear() {
        children.clear()
    }
}
