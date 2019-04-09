package kiwiband.dnb.actors.creatures

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2

/**
 * @param map currently contained map
 */
abstract class Creature(protected val map: LocalMap) : MapActor() {

    /**
     * Changes position of creature by [direction] and update the map
     */
    open fun move(direction: Vec2) {
        if (resolveCollision(map.getActors(pos + direction))) {
            val oldPos = Vec2(pos)
            pos.add(direction)
            map.actors.updateOne(oldPos)
        }
    }

    fun moveTo(position: Vec2M) = move(resolveDirection(position))

    private fun resolveDirection(position: Vec2M): Vec2M = (position - pos).normalize()

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

    override fun onDestroy() {
        super.onDestroy()
        map.actors.remove(pos, this)
    }
}
