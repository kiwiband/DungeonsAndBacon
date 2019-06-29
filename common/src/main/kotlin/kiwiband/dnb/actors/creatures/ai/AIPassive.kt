package kiwiband.dnb.actors.creatures.ai

import kiwiband.dnb.actors.creatures.Creature

class AIPassive() : AIBase() {

    override fun nextMove(pawn: Creature) {}

    override fun getID() = ID

    companion object {
        const val ID = 1
    }
}
