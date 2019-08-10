package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.LastElementBehavior.*

enum class LastElementBehavior {
    STOP, LOOP, DESELECT
}

abstract class InteractiveSequenceLayout<S : SequenceSlot, T, R, Child : InteractiveChildView<S, T, R>>(
    width: Int,
    height: Int,
    private val lastElementBehavior: LastElementBehavior
) : SequenceLayout<S, Child>(width, height) {

    var selected: Int = -1
        private set

    override fun addChild(view: View, slot: S): Child {
        return super.addChild(view, slot).also {
            if (selected == -1) {
                next()
            }
        }
    }

    fun next() = changeCurrent(1, { it >= children.size }, children.lastIndex, 0)

    fun previous() = changeCurrent(-1, { it < 0 }, 0, children.lastIndex)

    private fun changeCurrent(step: Int, isOut: (Int) -> Boolean, onStop: Int, onLoop: Int): Child? {
        if (children.isEmpty()) return null
        current()?.setInactive()
        selected += step
        if (isOut(selected)) {
            selected = when(lastElementBehavior) {
                STOP -> onStop
                LOOP -> onLoop
                DESELECT -> -1
            }
        }
        return current()?.also { it.setActive() }
    }

    fun current() = if (checkIndex(selected)) children[selected] else null

    fun interact(arg: T): R? = current()?.interact(arg)

    private fun checkIndex(ind: Int) = ind in 0..children.lastIndex
}


abstract class InteractiveHorizontalLayout<T, R, Child : InteractiveChildView<HorizontalSlot, T, R>>(
    width: Int,
    height: Int,
    lastElementBehavior: LastElementBehavior = LOOP
) : InteractiveSequenceLayout<HorizontalSlot, T, R, Child>(width, height, lastElementBehavior) {

    override val controller = SequenceLayoutHorizontalController(width, height, children)

    override fun defaultSlot() = HorizontalSlot()
}


abstract class InteractiveVerticalLayout<T, R, Child : InteractiveChildView<VerticalSlot, T, R>>(
    width: Int,
    height: Int,
    lastElementBehavior: LastElementBehavior = LOOP
) : InteractiveSequenceLayout<VerticalSlot, T, R, Child>(width, height, lastElementBehavior) {

    override val controller = SequenceLayoutVerticalController(width, height, children)

    override fun defaultSlot() = VerticalSlot()
}