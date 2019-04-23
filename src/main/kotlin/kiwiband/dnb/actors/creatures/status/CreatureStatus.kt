package kiwiband.dnb.actors.creatures.status

import kiwiband.dnb.math.MyMath
import kotlin.math.min
import kotlin.random.Random

class CreatureStatus(
    level: Int,
    health: Int = -1,
    experience: Int = 0
) {

    var maxHealth = 13 + 2 * level
        private set
    var maxExperience = 1 shl level
        private set

    var level = level
        private set
    var health: Int = if (health < 0) maxHealth else health
        private set
    var experience: Int = experience
        private set

    var weaponAttack = 0
    var armorDefence = 0

    fun getTotalAttack() = 2 + level + weaponAttack
    fun getTotalDefence() = (level + 1) / 2 + armorDefence

    fun addExperience(value: Int) {
        experience += value
        while (experience >= maxExperience) {
            experience -= maxExperience
            addLevel()
        }
    }

    fun addLevel() {
        level++
        maxExperience *= 2
        maxHealth += 2
    }

    fun addHealth(hp: Int) {
        health = MyMath.clamp(health + hp, 0, maxHealth)
    }

    fun damage(dmg: Int) {
        addHealth(min(0, getTotalDefence() - dmg))
    }

    companion object {

        fun generateDefault() = CreatureStatus(1)

        fun generateRandom() = CreatureStatus(Random.nextInt(3), Random.nextInt(2, 11))
    }
}