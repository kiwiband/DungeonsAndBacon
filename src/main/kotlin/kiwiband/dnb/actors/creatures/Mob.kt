package kiwiband.dnb.actors.creatures

import kiwiband.dnb.actors.creatures.ai.AIAggressive
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2M

class Mob(map: LocalMap, position: Vec2M) : Creature(map) {
    init {
        pos.set(position)
    }

    private val intelligence = AIAggressive(this, 5)
    override fun getViewAppearance() = '&'

    override fun onTick() {
        super.onTick()
        intelligence.nextMove()
    }
}