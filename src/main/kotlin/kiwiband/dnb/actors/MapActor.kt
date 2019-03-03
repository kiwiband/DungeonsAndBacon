package kiwiband.dnb.actors

import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2

open class MapActor {
    private val collision = Collision.Block
    val position = Vec2()

    open fun collide(actor: MapActor): Collision = collision.collide(actor.collision)

    open fun onBlock(actor: MapActor) {}

    open fun onOverlap(actor: MapActor) {}

    protected open fun blockInteract(actor: MapActor) {
        actor.onBlock(this)
    }

    protected open fun overlapInteract(actor: MapActor) {
        actor.onOverlap(this)
    }
}