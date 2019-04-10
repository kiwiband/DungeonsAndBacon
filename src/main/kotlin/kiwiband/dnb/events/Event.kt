package kiwiband.dnb.events

import com.googlecode.lanterna.input.KeyStroke
import kiwiband.dnb.math.Vec2
import java.util.*

/**
 * Base class for the events.
 */
sealed class Event

/**
 * Class dispatching the events.
 */
class EventDispatcher<T : Event> {
    private var id = 0

    private val handlers: MutableMap<Int, (T) -> Unit> = TreeMap()

    /**
     * @return added handler's id
     */
    fun addHandler(handler: (T) -> Unit): Int {
        handlers[id] = handler
        return id++
    }

    fun removeHandler(id: Int) = handlers.remove(id)

    /**
     * Send the [event] to all existing handlers
     */
    fun run(event: T) {
        handlers.values.forEach { it(event) }
    }
}

/**
 * Event for player movement
 * @param direction direction to move
 */
class EventMove(val direction: Vec2) : Event() {
    companion object {
        val dispatcher = EventDispatcher<EventMove>()
    }
}

/**
 * Event for game time tick.
 */
class EventTick : Event() {
    companion object {
        val dispatcher = EventDispatcher<EventTick>()
    }
}

/**
 * Event for key press.
 * @param key key pressed
 */
class EventKeyPress(val key: KeyStroke) : Event() {
    companion object {
        val dispatcher = EventDispatcher<EventKeyPress>()
    }
}
