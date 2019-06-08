package kiwiband.dnb.events

import com.googlecode.lanterna.input.KeyStroke
import kiwiband.dnb.JSONSerializable
import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2
import org.json.JSONObject

sealed class Event : JSONSerializable {
    override fun toJSON(): JSONObject {
        return JSONObject()
    }

    companion object {
        fun runFromJSON(json: JSONObject) {
            when (json["type"]) {
                "EventMove" -> {
                    val direction = json.getJSONObject("direction")
                    return EventMove.dispatcher.run(EventMove(Vec2(direction.getInt("x"), direction.getInt("y"))))
                }
                "EventItemUsed" -> {
                    return EventItemUsed.dispatcher.run(EventItemUsed(json.getInt("itemNum")))
                }
                else -> {
                    throw RuntimeException("Could not parse event from json")
                }
            }
        }
    }
}

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

    override fun toJSON(): JSONObject {
        return JSONObject().put("type", "EventMove").put("direction", direction.toJSON())
    }

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
 * Update the whole map.
 * @param newMap the actual map.
 */
class EventUpdateMap(val newMap: LocalMap) : Event() {

    companion object {
        val dispatcher = EventDispatcher<EventUpdateMap>()
    }
}

class EventItemUsed(val itemNum: Int): Event() {
    override fun toJSON(): JSONObject {
        return JSONObject().put("type", "EventItemUsed").put("itemNum", itemNum)
    }

    companion object {
        val dispatcher = EventDispatcher<EventItemUsed>()
    }
}