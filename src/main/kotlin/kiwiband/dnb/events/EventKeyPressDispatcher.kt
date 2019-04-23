package kiwiband.dnb.events

import java.util.*

data class DispatcherState(val id: Int,
                           val handlers: MutableMap<Int, (EventKeyPress) -> Unit>,
                           val removed: MutableList<Int>)

class EventKeyPressDispatcher: EventDispatcher<EventKeyPress>() {
    val statesStack = ArrayDeque<DispatcherState>()

    fun pushLayer() {
        statesStack.push(DispatcherState(id, handlers, removed))
        id = 0
        handlers = TreeMap()
        removed = mutableListOf()
    }

    fun popLayer() {
        val state = statesStack.pop()
        id = state.id
        handlers = state.handlers
        removed = state.removed
    }
}