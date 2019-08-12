package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.util.ChildView
import kiwiband.dnb.ui.views.layout.util.Slot

abstract class InteractiveChildView<S : Slot>(view: View, slot: S) :
    ChildView<S>(view, slot) {

    protected fun onInteract() {}

    abstract fun setActive()

    abstract fun setInactive()
}