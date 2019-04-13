package kiwiband.dnb.actors.creatures

import kiwiband.dnb.events.EventMove
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.math.Vec2M

/**
 * Player character.
 * @param map map where the character is on
 * @param position initial position on the [map]
 */
class Player(map: LocalMap, position: Vec2) : Creature(map) {
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
        EventMove.dispatcher.removeHandler(eventMoveId)
    }
}
