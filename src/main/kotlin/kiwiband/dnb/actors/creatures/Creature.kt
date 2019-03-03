package kiwiband.dnb.actors.creatures

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2M

abstract class Creature(val map: LocalMap) : MapActor() {
    open fun move(direction: Vec2M) {
        if (resolveCollision(map.getActor(position + direction))) {
            position.add(direction)
        }
    }

    private fun resolveCollision(actor: MapActor?): Boolean {
        actor ?: return true
        val cResult = collide(actor)
        return when (cResult) {
            Collision.Block -> {
                blockInteract(actor)
                false
            }
            Collision.Overlap -> {
                overlapInteract(actor)
                true
            }
            else -> true
        }
    }
}