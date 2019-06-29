package kiwiband.dnb.actors.creatures.ai

import kiwiband.dnb.actors.creatures.Creature
import kiwiband.dnb.math.Vec2
import kotlin.random.Random

class ConfusedAIDecorator(private var parentAI: AIBase) : AIBase() {
    init {
        parentAI = parentAI.getNormalizedAI()
    }

    private var effectTime = Random.nextInt(1, 5)

    override fun nextMove(pawn: Creature) {
        if (effectTime == 0) return parentAI.nextMove(pawn)
        val choice = Random.nextInt(4)
        pawn.move(when (choice) {
            0 -> Vec2(1, 0)
            1 -> Vec2(0, 1)
            2 -> Vec2(-1, 0)
            else -> Vec2(0, -1)
        })
        effectTime--
    }

    override fun getID() = parentAI.getID()

    override fun getNormalizedAI(): AIBase {
        return if (effectTime > 0) return this else parentAI.getNormalizedAI()
    }

    companion object {
        const val ID = -1
    }
}