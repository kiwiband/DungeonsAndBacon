package kiwiband.dnb.actors.creatures.ai

import kiwiband.dnb.actors.creatures.Creature
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.math.Vec2

class AIAggressive(pawn: Creature, private val viewRange: Int) : AIBase(pawn) {

    private var path: MutableList<Vec2>? = null

    override fun nextMove() {
        val map = pawn.map
        val res = map.navigationGraph.pathToNearest(pawn.pos, viewRange) { cell ->
            cell.any { actor ->
                actor is Player
            }
        }
        path = res ?: path
        if (path != null) {
            val p = path!!
            if (p.isNotEmpty()) {
                pawn.moveTo(p.last())
                p.removeAt(p.lastIndex)
            } else {
                path = null
            }
        }
    }

}