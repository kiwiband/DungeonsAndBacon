package kiwiband.dnb.events

import java.util.*

class EventPressKeyDispatcher internal constructor() : EventDispatcher<EventPressKey>() {

    private val statesStack = ArrayDeque<MutableList<EventHandler<EventPressKey>>>()

    private val permanentHandlers = mutableListOf<(EventPressKey) -> Unit>()

    override fun run(event: EventPressKey) {
        super.run(event)
        for (handler in permanentHandlers) {
            handler(event)
        }
    }

    fun addPermanentHandler(handler: (EventPressKey) -> Unit) {
        permanentHandlers.add(handler)
    }

    fun pushLayer() {
        statesStack.push(handlers)
        handlers = mutableListOf()
    }

    fun popLayer() {
        handlers = statesStack.pop()
    }
}