package kiwiband.dnb.actors

import kiwiband.dnb.events.EventTick
import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2M

enum class ViewOrder {
    Background, Default, Foreground;
}

/**
 * Base class for any actors on a map
 */
abstract class MapActor : Comparable<MapActor> {
    private var eventTickId: Int = -1
    open var viewOrder = ViewOrder.Default
    open var viewPriority = 0

    protected open val collision = Collision.Block
    open val pos = Vec2M()

    /**
     * @return result of colliding with the [actor]
     */
    open fun collide(actor: MapActor): Collision = collision.collide(actor.collision)

    /**
     * Performs actions when blocking the [actor]
     */
    open fun onBlock(actor: MapActor) {}

    /**
     * Performs actions when the [actor] blocked by THIS actor
     */
    open fun onOverlap(actor: MapActor) {}

    /**
     * Initialize actor and its primary events
     * Must be called when actor spawns on local map
     */
    open fun onBeginGame() {
        eventTickId = EventTick.dispatcher.addHandler { onTick() }
    }

    /**
     * Performs actions when blocked by the [actor]
     */
    protected open fun blockInteract(actor: MapActor) {
        actor.onBlock(this)
    }

    /**
     * Performs actions when overlapped by the [actor]
     */
    protected open fun overlapInteract(actor: MapActor) {
        actor.onOverlap(this)
    }

    /**
     * Performs actions on tick event
     */
    protected open fun onTick() {}

    /**
     * Perform actions on destroying creature
     */
    protected open fun onDestroy() {
        EventTick.dispatcher.removeHandler(eventTickId)
    }

    override fun compareTo(other: MapActor): Int = viewOrderCompare(other)

    private fun viewOrderCompare(other: MapActor): Int {
        return when(val firstOrder = viewOrder.compareTo(other.viewOrder)) {
            0 -> other.viewPriority - viewPriority
            else -> firstOrder
        }
    }

    abstract fun getViewAppearance(): Char
}
