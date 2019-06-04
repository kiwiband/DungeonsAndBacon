package kiwiband.dnb.events

import com.googlecode.lanterna.input.KeyStroke
import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.math.Vec2

sealed class Event

typealias EventHandlers<T> = MutableList<EventHandler<T>>

/**
 * Class dispatching the events.
 */
open class EventDispatcher<T : Event> {
    protected var handlers = mutableListOf<EventHandler<T>>()

    /**
     * @return added handler's registration.
     */
    open fun addHandler(handler: (T) -> Unit): Registration = EventHandler(handler).also { handlers.add(it) }

    /**
     * Sends the [event] to all existing handlers.
     */
    open fun run(event: T) {
        var actual = true
        handlers.forEach { if (it.isActive) it.run(event) else actual = false }
        handlers = if (actual) handlers else handlers.filter { it.isActive } as EventHandlers<T>
    }

    companion object {
        /**
         * Runs event in event handlers
         * @return actual event handlers
         */
        fun <T : Event> runInHandlers(event: T, handlers: EventHandlers<T>): EventHandlers<T> {
            var actual = true
            handlers.forEach { if (it.isActive) it.run(event) else actual = false }
            return if (actual) handlers else handlers.filter { it.isActive } as EventHandlers<T>
        }

        /**
         * Adds [handler] in [handlers]
         * @return added handler's registration.
         */
        fun <T : Event> addInHandlers(handler: (T) -> Unit, handlers: EventHandlers<T>): Registration {
            return EventHandler(handler).also { handlers.add(it) }
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
        val dispatcher = EventTickDispatcher()
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
class EventDestroyActor(val actor: MapActor) : Event() {
    companion object {
        val dispatcher = EventDispatcher<EventDestroyActor>()
    }
}

/**
 * Event for spawning actors.
 */
class EventSpawnActor(val actor: MapActor) : Event() {
    companion object {
        val dispatcher = EventDispatcher<EventSpawnActor>()
    }
}

/**
 * Event on finishing the activity with some result.
 */
abstract class EventActivityFinished<U>(val result: U) : Event()