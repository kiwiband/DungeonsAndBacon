package kiwiband.dnb.events

import com.googlecode.lanterna.input.KeyStroke
import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.math.Vec2

sealed class Event

/**
 * Class dispatching the events.
 */
open class EventDispatcher<T : Event> {
    protected var handlers = mutableListOf<EventHandler<T>>()

    /**
     * @return added handler's registration.
     */
    fun addHandler(handler: (T) -> Unit): Registration = EventHandler(handler).also { handlers.add(it) }

    /**
     * Sends the [event] to all existing handlers.
     */
    open fun run(event: T) {
        var needsFilter = false
        handlers.forEach { if (it.isActive) it.run(event) else needsFilter = true }
        if (needsFilter) {
            handlers = handlers.filter { it.isActive } as MutableList<EventHandler<T>>
        }
    }
}

open class EventHandler<T : Event>(private val handler: (T) -> Unit) : Registration {
    var isActive = true

    fun run(event: T) {
        handler(event)
    }

    override fun finish() {
        isActive = false
    }
}

/**
 * Event for player movement.
 * @param direction direction to move.
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
 * Event for game over.
 */
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
 * Event for destroying actors.
 */
class EventDestroyActor(val actor: MapActor): Event() {
    companion object {
        val dispatcher = EventDispatcher<EventDestroyActor>()
    }
}

/**
 * Event for spawning actors.
 */
class EventSpawnActor(val actor: MapActor): Event() {
    companion object {
        val dispatcher = EventDispatcher<EventSpawnActor>()
    }
}

/**
 * Event on finishing the activity with some result.
 */
abstract class EventActivityFinished<U>(val result: U): Event()