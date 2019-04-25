package kiwiband.dnb.events

import java.util.*

data class DispatcherState(val id: Int,
                           val handlers: MutableMap<Int, (EventKeyPress) -> Unit>,
                           val removed: MutableList<Int>)

class EventKeyPressDispatcher: EventDispatcher<EventKeyPress>() {
    private val statesStack = ArrayDeque<DispatcherState>()

    private val permanentHandlers = mutableListOf<(EventKeyPress) -> Unit>()

    override fun run(event: EventKeyPress) {
        super.run(event)
        for (handler in permanentHandlers) {
            handler(event)
        }
    }

    fun addPermanentHandler(handler: (EventKeyPress) -> Unit) {
        permanentHandlers.add(handler)
    }

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