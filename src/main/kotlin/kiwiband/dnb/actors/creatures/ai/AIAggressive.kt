package kiwiband.dnb.actors.creatures.ai

import kiwiband.dnb.actors.creatures.Creature
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.math.Vec2M

class AIAggressive(pawn: Creature, private val viewRange: Int) : AIBase(pawn) {
//    private val viewBorders = Vec2M(-viewRange) to Vec2M(viewRange + 1)
    private var path: MutableList<Vec2M>? = null

    override fun nextMove() {
        val map = pawn.map
        val res = map.navigationGraph.bfs(pawn.pos, viewRange) {
            cell -> cell.any {
                actor -> actor is Player }
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