package kiwiband.dnb.actors.creatures.status

import kiwiband.dnb.math.MyMath
import kotlin.math.min
import kotlin.random.Random

data class CreatureStatus(
    var maxHealth: Int,
    var attack: Int,
    var defence: Int
) {

    var level: Int = 1
    var experience: Int = 0
    var health = maxHealth
    var maxExperience = 2

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
        if (level % 2 == 0) {
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

        val DEFAULT = CreatureStatus(15, 3, 1)

        fun generateRandom(): CreatureStatus {
            val health = Random.nextInt(5, 16)
            val attack = Random.nextInt(8)
            val defence = Random.nextInt(2)
            return CreatureStatus(health, attack, defence)
        }
    }
}