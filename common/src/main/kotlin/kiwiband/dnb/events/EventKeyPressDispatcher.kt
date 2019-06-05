package kiwiband.dnb.events

import java.util.*

class EventKeyPressDispatcher: EventDispatcher<EventKeyPress>() {
    private val statesStack = ArrayDeque<MutableList<EventHandler<EventKeyPress>>>()

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
        statesStack.push(handlers)
        handlers = mutableListOf()
    }

    fun popLayer() {
        handlers = statesStack.pop()
    }
}