package kiwiband.dnb.actors.creatures

import kiwiband.dnb.Colors
import kiwiband.dnb.actors.ViewAppearance
import kiwiband.dnb.actors.creatures.ai.AIAggressive
import kiwiband.dnb.actors.creatures.ai.AIPassive
import kiwiband.dnb.actors.creatures.ai.AITimid
import kiwiband.dnb.actors.creatures.status.CreatureStatus
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2
import kotlin.random.Random

object MobFactory {
    fun createAggressiveMob(map: LocalMap, position: Vec2, status: CreatureStatus): Mob {
        return Mob(map, position, AIAggressive(AIAggressive.VIEW_RANGE), ViewAppearance('&', Colors.RED), status)
    }

    fun createTimidMob(map: LocalMap, position: Vec2, status: CreatureStatus): Mob {
        return Mob(map, position, AITimid(AITimid.VIEW_RANGE), ViewAppearance('%'), status)
    }

    fun createPassiveMob(map: LocalMap, position: Vec2, status: CreatureStatus): Mob {
        return Mob(map, position, AIPassive(), ViewAppearance('$'), status)
    }

    fun createMob(map: LocalMap, position: Vec2, status: CreatureStatus) =
        createMob(map, position, status, Random.nextInt(3))

    fun createMob(map: LocalMap, position: Vec2, status: CreatureStatus, aiID: Int): Mob {
        return when (aiID) {
            AIAggressive.ID -> createAggressiveMob(map, position, status)
            AITimid.ID -> createTimidMob(map, position, status)
            AIPassive.ID -> createPassiveMob(map, position, status)
            else -> createPassiveMob(map, position, status)
            //TODO add confused mob
        }
    }
}