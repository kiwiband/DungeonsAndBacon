package kiwiband.dnb.actors.creatures.ai

import kiwiband.dnb.actors.creatures.Creature
import kiwiband.dnb.actors.creatures.Player

class AITimid(pawn: Creature, private val viewRange: Int) : AIBase(pawn) {

    override fun nextMove() {
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
}
