package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.util.ChildView
import kiwiband.dnb.ui.views.layout.util.Padding
import kiwiband.dnb.ui.views.layout.util.Slot

abstract class InteractiveChildView<S : Slot>(view: View, slot: S) :
    ChildView<S>(view, slot) {

    open fun onInteract() {}

    abstract fun setActive()

    abstract fun setInactive()
}

open class BoxedChildView<S : Slot>(
    view: View,
    slot: S
) : InteractiveChildView<S>(view, slot) {
    private val unselectedView = WrapperLayout(view, WrapperSlot(padding = Padding(1)))
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