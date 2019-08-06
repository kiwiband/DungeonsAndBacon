package kiwiband.dnb.ui.views

import kiwiband.dnb.actors.creatures.status.CreatureStatus
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.Renderer

object Drawer {
    fun drawCreatureStatus(renderer: Renderer, status: CreatureStatus, x: Int, y: Int) {
        val pos = Vec2M(x, y)
        renderer.writeText("HP ${status.health}/${status.maxHealth}", pos)
        renderer.writeText("LVL ${status.level}", pos.add(0, 1))
        renderer.writeText("EXP ${status.experience}/${status.maxExperience}", pos.add(0, 1))
        renderer.writeText("ATK ${status.getTotalAttack()}", pos.add(0, 1))
        renderer.writeText("DEF ${status.getTotalDefence()}", pos.add(0, 1))
    }
}