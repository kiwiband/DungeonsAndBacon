package kiwiband.dnb.actors

import kiwiband.dnb.events.EventTick
import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2

abstract class MapActor {
    private var eventTickId: Int = -1

    protected open val collision = Collision.Block
    val position = Vec2()

    open fun collide(actor: MapActor): Collision = collision.collide(actor.collision)

    open fun onBlock(actor: MapActor) {}

    open fun onOverlap(actor: MapActor) {}

    // Should be called when actor spawns on local map
    open fun onBeginGame() {
        eventTickId = EventTick.dispatcher.addHandler { onTick() }
    }

    protected open fun blockInteract(actor: MapActor) {
        actor.onBlock(this)
    }

    protected open fun overlapInteract(actor: MapActor) {
        actor.onOverlap(this)
    }

    protected open fun onTick() {}

    protected open fun onDestroy() {
        EventTick.dispatcher.removeHandler(eventTickId)
    }
}