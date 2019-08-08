package kiwiband.dnb.events

import com.googlecode.lanterna.input.KeyStroke
import kiwiband.dnb.JSONSerializable
import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2
import org.json.JSONObject

sealed class Event : JSONSerializable {

    /**
     * Serializes the event to JSON.
     * @return JSON object
     */
    override fun toJSON(): JSONObject {
        return JSONObject().put("t", this.javaClass.simpleName)
    }

    /**
     * @inherit
     *
     * Generate a string with JSON representation of the event
     * @return Json string
     */
    override fun toString(): String {
        return toJSON().toString()
    }
}

/**
 * Event for player movement.
 * @param direction direction to move.
 */
class EventMove(val direction: Vec2, val playerId: String) : Event() {
    override fun toJSON(): JSONObject {
        return super.toJSON().put("dir", direction.toJSON()).put("id", playerId)
    }
}

/**
 * Event for game time tick.
 */
class EventTick : Event()

/**
 * Event for game over.
 */
class EventGameOver : Event()

/**
 * Event for key press.
 * @param key key pressed
 */
class EventPressKey(val key: KeyStroke) : Event()

/**
 * Event for destroying actors.
 */
class EventDestroyActor(val actor: MapActor) : Event()

/**
 * Event for spawning actors.
 */
class EventSpawnActor(val actor: MapActor) : Event()

/**
 * Update the whole map.
 * @param newMap the actual map.
 */
class EventUpdateMap(val newMap: LocalMap) : Event()


class EventUseItem(val itemNum: Int, val playerId: String) : Event() {
    override fun toJSON(): JSONObject {
        return super.toJSON().put("itm", itemNum).put("id", playerId)
    }
}