package kiwiband.dnb.actors.creatures.ai

import kiwiband.dnb.actors.creatures.Creature

abstract class AIBase() {
    abstract fun nextMove(pawn: Creature)

    abstract fun getID(): Int

    open fun getNormalizedAI() = this
}