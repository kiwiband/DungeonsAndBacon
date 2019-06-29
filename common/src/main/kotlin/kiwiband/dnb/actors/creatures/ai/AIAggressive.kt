package kiwiband.dnb.actors.creatures.ai

import kiwiband.dnb.actors.creatures.Creature
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.math.Vec2

class AIAggressive(private val viewRange: Int) : AIBase() {

    private var path: MutableList<Vec2>? = null

    override fun nextMove(pawn: Creature) {
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

    override fun getID() = ID

    companion object {
        const val VIEW_RANGE = 5
        const val ID = 0
    }
}