package kiwiband.dnb.actors.creatures

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2M

abstract class Creature(private val map: LocalMap) : MapActor() {
    open fun move(direction: Vec2M) {
        if (resolveCollision(map.getActors(pos + direction))) {
            val oldPos = Vec2M(pos)
            pos.add(direction)
            map.actors.updateOne(oldPos)
        }
    }

    private fun resolveCollision(actors: Collection<MapActor>): Boolean {
        var result = true
        for (actor in actors) {
            val cResult = collide(actor)
            when (cResult) {
                Collision.Block -> {
                    blockInteract(actor)
                    result = false
                }
                Collision.Overlap -> overlapInteract(actor)
                else -> {
                }
            }
        }
        return result
    }
}
