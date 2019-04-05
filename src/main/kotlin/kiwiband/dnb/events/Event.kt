package kiwiband.dnb.events

import com.googlecode.lanterna.input.KeyStroke
import kiwiband.dnb.math.Vec2
import java.util.*

sealed class Event

class EventDispatcher<T : Event> {
    private var id = 0

    private val handlers: MutableMap<Int, (T) -> Unit> = TreeMap()

    /**
     * Add handler to others and return handler's id
     */
    fun addHandler(handler: (T) -> Unit): Int {
        handlers[id++] = handler
        return id - 1
    }

    fun removeHandler(id: Int) = handlers.remove(id)

    fun run(event: T) {
        handlers.values.forEach { it(event) }
    }
}

class EventMove(val direction: Vec2) : Event() {
    companion object {
        val dispatcher = EventDispatcher<EventMove>()
    }
}

class EventTick : Event() {
    companion object {
        val dispatcher = EventDispatcher<EventTick>()
    }
}

class EventKeyPress(val key: KeyStroke) : Event() {
    companion object {
        val dispatcher = EventDispatcher<EventKeyPress>()
    }
}
