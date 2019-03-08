package kiwiband.dnb.actors

import kiwiband.dnb.events.EventTick
import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2

enum class ViewOrder(private val order: Int) {
    Background(0), Default(1), Foreground(2);

    fun compare(other: ViewOrder): Int = other.order - order
}

abstract class MapActor {
    private var eventTickId: Int = -1
    open var viewOrder = ViewOrder.Default
    open var viewPriority = 0

    protected open val collision = Collision.Block
    val pos = Vec2()

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

    fun viewOrderCompare(other: MapActor): Int {
        return when(val firstOrder = viewOrder.compare(other.viewOrder)) {
            0 -> other.viewPriority - viewPriority
            else -> firstOrder
        }
    }

    abstract fun getViewAppearance(): Char
}
