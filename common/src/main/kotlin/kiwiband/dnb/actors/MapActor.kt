package kiwiband.dnb.actors

import kiwiband.dnb.Game
import kiwiband.dnb.JSONSerializable
import kiwiband.dnb.events.*
import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2M
import org.json.JSONObject

enum class ViewOrder {
    Background, Default, Foreground;
}

/**
 * Base class for all actors on a map
 */
abstract class MapActor(
    private val tickOrder: TickOrder = TickOrder.DEFAULT,
    protected var game: Game? = null
) : Comparable<MapActor>, JSONSerializable {
    private var eventTick: Registration? = null
    open var viewOrder = ViewOrder.Default
    open var viewPriority = 0

    open val collision = Collision.Block
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
     * Initializes the actor and its primary events
     * Must be called when an actor spawns on a local map
     */
    open fun onBeginGame(game: Game) {
        this.game = game
        eventTick = game.eventBus.eventTick.addHandler(tickOrder) { onTick() }
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
     * Destroys this actor
     */
    fun destroy() {
        game?.eventBus?.run(EventDestroyActor(this))
        onDestroy()
    }

    /**
     * Performs actions on destroying a creature
     */
    protected open fun onDestroy() {
        eventTick?.finish()
    }

    override fun compareTo(other: MapActor): Int {
        return when (val firstOrder = viewOrder.compareTo(other.viewOrder)) {
            0 -> other.viewPriority - viewPriority
            else -> firstOrder
        }
    }


    override fun toJSON(): JSONObject = JSONObject().put("x", pos.x).put("y", pos.y).put("t", getType())

    abstract fun getType(): String

    abstract fun getViewAppearance(): Char
}
