package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.LastElementBehavior.*

enum class LastElementBehavior {
    STOP, LOOP, DESELECT
}

abstract class InteractiveSequenceLayout<S : SequenceSlot, Child : InteractiveChildView<S>>(
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

    fun next() = changeCurrent(1, children.lastIndex, 0)

    fun previous() = changeCurrent(-1, 0, children.lastIndex)

    private fun changeCurrent(step: Int, onStop: Int, onLoop: Int): Child? {
        if (children.isEmpty()) return null
        current()?.setInactive()
        selected += step
        if (selected !in children.indices) {
            selected = when(lastElementBehavior) {
                STOP -> onStop
                LOOP -> onLoop
                DESELECT -> -1
            }
        }
        return current()?.also { it.setActive() }
    }

    fun current() = children.getOrNull(selected)

    fun interact(f: Child.() -> Unit) = current()?.also { it.f() }
}

abstract class InteractiveVerticalLayout<Child : InteractiveChildView<VerticalSlot>>(
    width: Int,
    height: Int,
    lastElementBehavior: LastElementBehavior = LOOP
) : InteractiveSequenceLayout<VerticalSlot, Child>(width, height, lastElementBehavior) {

    override val controller = SequenceLayoutVerticalController(width, height, children)

    override fun defaultSlot() = VerticalSlot()
}