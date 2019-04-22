package kiwiband.dnb.actors.creatures.status

import kiwiband.dnb.math.MyMath
import org.json.JSONObject
import kotlin.math.min
import kotlin.random.Random

class CreatureStatus(
    var level: Int,
    var health: Int = -1,
    var experience: Int = 0
) {

    var maxHealth = 13 + 2 * level
    var attack = 2 + level
    var defence = (level + 1) / 2
    var maxExperience = 1.shl(level)

    init {
        if (health < 0) {
            health = maxHealth
        }
    }

    fun addExperience() {
        experience++
        if (experience >= maxExperience) {
            experience = 0
            addLevel()
        }
    }

    fun addLevel() {
        level++
        maxExperience *= 2
        maxHealth += 2
        attack++
        if (level % 2 == 1) {
            defence++
        }
    }

    fun addHealth(hp: Int) {
        health = MyMath.clamp(health + hp, 0, maxHealth)
    }

    fun damage(dmg: Int) {
        addHealth(min(0, defence - dmg))
    }

    companion object {

        fun generateDefault() = CreatureStatus(1)

        fun generateRandom() = CreatureStatus(Random.nextInt(3), Random.nextInt(2, 11))
    }
}