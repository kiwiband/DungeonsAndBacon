package kiwiband.dnb.actors.creatures

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.creatures.status.CreatureStatus
import kiwiband.dnb.events.EventGameOver
import kiwiband.dnb.events.EventMove
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.math.Vec2M
import kotlin.random.Random

/**
 * Player character.
 * @param map map where the character is on
 * @param position initial position on the [map]
 */
class Player(map: LocalMap, position: Vec2, status: CreatureStatus) : Creature(map, status) {
    private val viewAppearance = '@'

    init {
        super.pos.set(position)
    }

    override fun getViewAppearance(): Char = viewAppearance

    private var eventMoveId: Int = -1
    private val moveDirection: Vec2M = Vec2M()
    override var viewPriority: Int = 10000

    override fun onBeginGame() {
        super.onBeginGame()
        eventMoveId = EventMove.dispatcher.addHandler { moveDirection.set(it.direction) }
    }

    override fun onTick() {
        super.onTick()
        if (!moveDirection.isZero()) {
            move(moveDirection)
            moveDirection.set(0, 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventGameOver.dispatcher.run(EventGameOver())
        EventMove.dispatcher.removeHandler(eventMoveId)
    }

    override fun blockInteract(actor: MapActor): Boolean {
        if (super.blockInteract(actor)) {
            status.addExperience()
            status.addHealth(1)
            return true
        }
        if (actor is Mob && Random.nextFloat() < 0.2) {
            actor.confuse()
        }
        return false
    }

    override fun getType() = TYPE_ID

    companion object {
        const val TYPE_ID = "plyr"
    }
}
