package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.util.Slot
import kiwiband.dnb.ui.views.layout.util.Padding
import kiwiband.dnb.ui.views.layout.util.Node

abstract class InteractiveSlot<N : Node>(view: View, node: N) : Slot<N>(view, node) {

    open fun onInteract() {}

    abstract fun setActive()

    abstract fun setInactive()
}

open class BoxedSlot<N : Node>(
    view: View,
    node: N
) : InteractiveSlot<N>(view, node) {
    private val unselectedView = WrapperLayout(view, WrapperNode(padding = Padding(1)))
    private val selectedView = BoxLayout(view)

    init {
        setInactive()
    }

    override fun setActive() {
        this.view = selectedView
    }

    override fun setInactive() {
        this.view = unselectedView
    }
}