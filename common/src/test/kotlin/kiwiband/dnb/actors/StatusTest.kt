package kiwiband.dnb.actors

import kiwiband.dnb.actors.creatures.status.CreatureStatus
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class StatusTest {

    @Test
    fun minMaxHpTest() {
        val status = CreatureStatus(10, 20)
        val rand = Random(123)
        for (i in 0..50) {
            status.addHealth(rand.nextInt(-10 - status.health, 10 + status.maxHealth))
            assertTrue(0 <= status.health && status.health <= status.maxHealth)
        }
    }

    @Test
    fun minMaxExpTest() {
        val status = CreatureStatus(10, experience = 5)
        val rand = Random(123)
        for (i in 0..50) {
            status.addExperience(rand.nextInt(-10 - status.experience, 10 + status.maxExperience))
            assertTrue(0 <= status.experience && status.experience <= status.maxExperience)
        }
    }

    @Test
    fun nextLevelTest() {
        val status = CreatureStatus(10, experience = 5)
        val previousLevel = status.level
        status.addExperience(status.maxExperience)
        assertTrue(previousLevel < status.level)
    }

}