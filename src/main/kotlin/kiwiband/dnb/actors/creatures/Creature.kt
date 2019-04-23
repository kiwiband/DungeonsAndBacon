package kiwiband.dnb.actors.creatures

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.creatures.status.CreatureStatus
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2
import org.json.JSONObject

/**
 * @param map currently contained map
 */
abstract class Creature(val map: LocalMap, val status: CreatureStatus) : MapActor() {

    /**
     * Changes position of creature by [direction] and update the map
     */
    open fun move(direction: Vec2) {
        val oldPos = Vec2(pos)
        if (resolveCollision(map.getActors(pos + direction))) {
            pos.add(direction)
            map.actors.updateOne(oldPos)
        } else {
            map.actors.filterDead(pos + direction)
        }
    }

    fun moveTo(position: Vec2) = move(resolveDirection(position))

    private fun resolveDirection(position: Vec2): Vec2 = (position - pos).normalize()

    private fun resolveCollision(actors: Collection<MapActor>): Boolean {
        var result = true
        for (actor in actors) {
            when (collide(actor)) {
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

    override fun blockInteract(actor: MapActor) {
        super.blockInteract(actor)
        if (actor is Creature) {
            actor.hit(this, status.getTotalAttack())
        }
    }

    fun hit(creature: Creature, dmg: Int) {
        status.damage(dmg)
        if (isDead()) {
            creature.status.addExperience(status.level)
            creature.status.addHealth(1)
        }
    }

    fun isDead(): Boolean = status.health == 0

    fun checkDead(): Boolean {
        if (isDead()) {
            onDestroy()
        }
        return isDead()
    }

    override fun toJSON() = super.toJSON()
        .put("lvl", status.level)
        .put("hp", status.health)
        .put("exp", status.experience)

}
