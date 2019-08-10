package kiwiband.dnb.ui.views.layout.util

import kiwiband.dnb.ui.views.View

/**
 * Data class containing the information about the child views' positions.
 * @param view view
 */
open class ChildView<S : Slot>(var view: View, open val slot: S)

/**
 * Base class for layouts - container views.
 */
abstract class Layout<S : Slot, Child : ChildView<S>>(width: Int, height: Int) : View(width, height) {
    /**
     * List of all views in layout
     */
    protected val children = mutableListOf<Child>()

    /**
     * Removes all the children from the layout.
     */
    open fun clear() {
        children.clear()
    }
}
