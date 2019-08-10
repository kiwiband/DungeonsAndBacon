package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.util.ChildView
import kiwiband.dnb.ui.views.layout.util.Slot

abstract class InteractiveChildView<S : Slot, T, R>(view: View, slot: S, private val interact: (T) -> R) :
    ChildView<S>(view, slot) {

    fun interact(arg: T) : R {
        return interact.invoke(arg).also { onInteract() }
    }

    protected fun onInteract() {}

    abstract fun setActive()

    abstract fun setInactive()
}