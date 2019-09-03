package kiwiband.dnb.ui.views.layout.util

import kiwiband.dnb.ui.views.View

/**
 * Data class containing the information about the child views' positions.
 * @param view view
 */
open class Slot<N : Node>(var view: View, open val node: N)

/**
 * Base class for layouts - container views.
 */
abstract class Layout<N : Node, S : Slot<N>>(width: Int, height: Int) : View(width, height) {
    /**
     * List of all views in layout
     */
    protected val children = mutableListOf<S>()

    /**
     * Removes all the children from the layout.
     */
    open fun clear() {
        children.clear()
    }

    override fun resize(width: Int, height: Int) {
        setSize(width, height)
        updateSize()
    }

    abstract fun updateSize()
}
