package kiwiband.dnb.actors.creatures.ai

import kiwiband.dnb.actors.creatures.Creature

abstract class AIBase(val pawn: Creature) {
    abstract fun nextMove()
}