package kiwiband.dnb.events

import kiwiband.dnb.math.Vec2
import org.json.JSONObject
import java.lang.RuntimeException

class EventBus {
    val eventMove = EventDispatcher<EventMove>()
    val eventTick = EventTickDispatcher()
    val eventGameOver = EventDispatcher<EventGameOver>()
    val eventKeyPress = EventKeyPressDispatcher()
    val eventDestroyActor = EventDispatcher<EventDestroyActor>()
    val eventSpawnActor = EventDispatcher<EventSpawnActor>()
    val eventUpdateMap = EventDispatcher<EventUpdateMap>()
    val eventItemUsed = EventDispatcher<EventItemUsed>()

    fun run(event: Event) {
        when (event) {
            is EventMove -> eventMove.run(event)
            is EventTick -> eventTick.run(event)
            is EventGameOver -> eventGameOver.run(event)
            is EventKeyPress -> eventKeyPress.run(event)
            is EventDestroyActor -> eventDestroyActor.run(event)
            is EventSpawnActor -> eventSpawnActor.run(event)
            is EventUpdateMap -> eventUpdateMap.run(event)
            is EventItemUsed -> eventItemUsed.run(event)
        }.let { /* exhaustive when */ }
    }

    fun runFromJSON(json: JSONObject) {
        return when (json["t"]) {
            "EventMove" -> {
                val dir = json.getJSONObject("dir")
                run(EventMove(Vec2(dir.getInt("x"), dir.getInt("y")), json.getInt("id")))
            }
            "EventItemUsed" -> {
                run(EventItemUsed(json.getInt("itm")))
            }
            else -> {
                throw RuntimeException("Could not parse event from json")
            }
        }
    }
}