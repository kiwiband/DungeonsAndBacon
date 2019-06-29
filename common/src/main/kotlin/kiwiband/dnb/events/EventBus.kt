package kiwiband.dnb.events

import kiwiband.dnb.math.Vec2
import org.json.JSONObject
import java.lang.RuntimeException

class EventBus {
    val move = EventDispatcher<EventMove>()
    val tick = EventTickDispatcher()
    val gameOver = EventDispatcher<EventGameOver>()
    val pressKey = EventPressKeyDispatcher()
    val destroyActor = EventDispatcher<EventDestroyActor>()
    val spawnActor = EventDispatcher<EventSpawnActor>()
    val updateMap = EventDispatcher<EventUpdateMap>()
    val useItem = EventDispatcher<EventUseItem>()

    fun run(event: Event) = when (event) {
        is EventMove -> move.run(event)
        is EventTick -> tick.run(event)
        is EventGameOver -> gameOver.run(event)
        is EventPressKey -> pressKey.run(event)
        is EventDestroyActor -> destroyActor.run(event)
        is EventSpawnActor -> spawnActor.run(event)
        is EventUpdateMap -> updateMap.run(event)
        is EventUseItem -> useItem.run(event)
    }

    fun runFromJSON(json: JSONObject) {
        return when (json["t"]) {
            "EventMove" -> {
                val dir = json.getJSONObject("dir")
                run(EventMove(Vec2(dir.getInt("x"), dir.getInt("y")), json.getInt("id")))
            }
            "EventUseItem" -> {
                run(EventUseItem(json.getInt("itm"), json.getInt("id")))
            }
            else -> {
                throw RuntimeException("Could not parse event from json")
            }
        }
    }
}