package kiwiband.dnb.actors.creatures

import kiwiband.dnb.actors.creatures.ai.AIAggressive
import kiwiband.dnb.actors.creatures.ai.AIBase
import kiwiband.dnb.actors.creatures.ai.AIPassive
import kiwiband.dnb.actors.creatures.ai.AITimid
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2M
import kotlin.random.Random

class Mob(map: LocalMap, position: Vec2M) : Creature(map) {

    private val intelligence: AIBase
    override fun getViewAppearance() = '&'

    init {
        intelligence = when (Random.nextInt(3)) {
            0 -> AITimid(this, VIEW_RANGE)
            1 -> AIPassive(this)
            else -> AIAggressive(this, VIEW_RANGE)
        }
        pos.set(position)
    }

    override fun onTick() {
        super.onTick()
        intelligence.nextMove()
    }

    companion object {
        private const val VIEW_RANGE = 5
    }
}