package kiwiband.dnb.actors.creatures.ai

import kiwiband.dnb.math.Vec2
import kotlin.random.Random

class ConfusedAIDecorator(private var parentAI: AIBase) : AIBase(parentAI.pawn) {
    private val random = Random(System.currentTimeMillis())
    private var effectTime = random.nextInt(1, 5)

    override fun nextMove() {
        if (effectTime == 0) return parentAI.nextMove()
        val choice = random.nextInt(4)
        pawn.move(when (choice) {
            0 -> Vec2(1, 0)
            1 -> Vec2(0, 1)
            2 -> Vec2(-1, 0)
            else -> Vec2(0, -1)
        })
        effectTime--
    }

    /**
     * @return this decorator
     */
    fun removeFinishedDecorators(): ConfusedAIDecorator {
        while (parentAI is ConfusedAIDecorator) {
            val parentDecorator = parentAI as ConfusedAIDecorator
            if (parentDecorator.effectTime == 0) {
                parentAI = parentDecorator.parentAI
            } else {
                break
            }
        }
        return this
    }
}