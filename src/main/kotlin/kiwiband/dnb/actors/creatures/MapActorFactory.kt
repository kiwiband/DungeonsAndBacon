package kiwiband.dnb.actors.creatures

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.statics.WallActor
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2M
import org.json.JSONObject

object MapActorFactory {
    fun createMapActor(map: LocalMap, actorJSON: JSONObject): MapActor? {
        val x = actorJSON.getInt("x")
        val y = actorJSON.getInt("y")
        return when (actorJSON.getString("t")) {
            WallActor.TYPE_ID -> WallActor(Vec2M(x, y))
            Player.TYPE_ID -> Player(map, Vec2M(x, y))
            Mob.TYPE_ID -> Mob(map, Vec2M(x, y), actorJSON.getInt("ai"))
            else -> null
        }
    }
}