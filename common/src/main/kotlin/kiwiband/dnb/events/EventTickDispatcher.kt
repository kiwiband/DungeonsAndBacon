package kiwiband.dnb.events

import kiwiband.dnb.events.TickOrder.*

private typealias TickHandler = EventHandler<EventTick>

class EventTickDispatcher internal constructor(): EventDispatcher<EventTick>() {
    private var firstHandlers = mutableListOf<TickHandler>()
    private var playerHandlers = mutableListOf<TickHandler>()
    private var beforeDrawUIHandlers = mutableListOf<TickHandler>()
    private var drawUIHandlers = mutableListOf<TickHandler>()
    private var lastHandlers = mutableListOf<TickHandler>()


    override fun run(event: EventTick) {
        firstHandlers = runInHandlers(event, firstHandlers)
        playerHandlers = runInHandlers(event, playerHandlers)
        handlers = runInHandlers(event, handlers)
        beforeDrawUIHandlers = runInHandlers(event, beforeDrawUIHandlers)
        drawUIHandlers = runInHandlers(event, drawUIHandlers)
        lastHandlers = runInHandlers(event, lastHandlers)
    }

    override fun addHandler(handler: (EventTick) -> Unit) = addHandler(DEFAULT, handler)

    fun addHandler(order: TickOrder, handler: (EventTick) -> Unit): Registration = when (order) {
        FIRST -> addInHandlers(handler, firstHandlers)
        PLAYER -> addInHandlers(handler, playerHandlers)
        DEFAULT -> addInHandlers(handler, handlers)
        BEFORE_DRAW_UI -> addInHandlers(handler, beforeDrawUIHandlers)
        DRAW_UI -> addInHandlers(handler, drawUIHandlers)
        LAST -> addInHandlers(handler, lastHandlers)
    }
}