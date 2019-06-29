package kiwiband.dnb.actors.creatures.ai

import kiwiband.dnb.actors.creatures.Creature
import kiwiband.dnb.actors.creatures.Player

class AITimid(private val viewRange: Int) : AIBase() {

    override fun nextMove(pawn: Creature) {
        val graph = pawn.map.navigationGraph
        val nearestPlayer = graph.nearest(pawn.pos, viewRange) { cell ->
            cell.any { actor ->
                actor is Player
            }
        } ?: return
        val nextCell = graph.nextToNearest(nearestPlayer, viewRange) { cell ->
            cell.contains(pawn)
        } ?: return
        assert(nextCell.distance(pawn.pos) == 1)
        pawn.moveTo(nextCell)
    }

    override fun getID() = ID

    companion object {
        const val VIEW_RANGE = 5
        const val ID = 2
    }
}
