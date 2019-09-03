package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.views.layout.LastElementBehavior.*

enum class LastElementBehavior {
    STOP, LOOP, DESELECT
}

abstract class InteractiveSequenceLayout<N : SequenceNode, S : InteractiveSlot<N>>(
    width: Int,
    height: Int,
    private val lastElementBehavior: LastElementBehavior
) : SequenceLayout<N, S>(width, height) {

    var selected: Int = -1
        private set

    override fun add(child: S): S {
        return super.add(child).also {
            if (selected == -1) {
                next()
            }
        }
    }

    fun next() = changeCurrent(1, children.lastIndex, 0)

    fun previous() = changeCurrent(-1, 0, children.lastIndex)

    private fun changeCurrent(step: Int, onStop: Int, onLoop: Int): S? {
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

    fun interact() = current()?.onInteract()

    override fun clear() {
        super.clear()
        selected = -1;
    }
}

abstract class InteractiveVerticalLayout<S : InteractiveSlot<VerticalNode>>(
    width: Int,
    height: Int,
    lastElementBehavior: LastElementBehavior = LOOP
) : InteractiveSequenceLayout<VerticalNode, S>(width, height, lastElementBehavior) {

    override val controller = VerticalSlotController(width, height, children)

    override fun defaultNode() = VerticalNode()
}