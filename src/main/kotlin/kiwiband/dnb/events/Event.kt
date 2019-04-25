package kiwiband.dnb.events

import com.googlecode.lanterna.input.KeyStroke
import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.math.Vec2
import java.util.*

sealed class Event

/**
 * Class dispatching the events.
 */
open class EventDispatcher<T : Event> {
    protected var id = 0

    protected var handlers: MutableMap<Int, (T) -> Unit> = TreeMap()

    protected var removed: MutableList<Int> = mutableListOf()

    /**
     * @return added handler's id
     */
    fun addHandler(handler: (T) -> Unit): Int {
        handlers[id] = handler
        return id++
    }

    fun removeHandler(id: Int) = removed.add(id)


    /**
     * Sends the [event] to all existing handlers
     */
    open fun run(event: T) {
        removed.forEach { handlers.remove(it) }
        removed.clear()
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


class EventGameOver : Event() {
    companion object {
        val dispatcher = EventDispatcher<EventGameOver>()
    }
}

/**
 * Event for key press.
 * @param key key pressed
 */
class EventKeyPress(val key: KeyStroke) : Event() {
    companion object {
        val dispatcher = EventKeyPressDispatcher()
    }
}

/**
 * Event for destroying actors
 */
class EventDestroyActor(val actor: MapActor): Event() {
    companion object {
        val dispatcher = EventDispatcher<EventDestroyActor>()
    }
}

class EventSpawnActor(val actor: MapActor): Event() {
    companion object {
        val dispatcher = EventDispatcher<EventSpawnActor>()
    }
}
