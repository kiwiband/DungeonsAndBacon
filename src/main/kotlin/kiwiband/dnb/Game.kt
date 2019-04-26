package kiwiband.dnb

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.events.*
import kiwiband.dnb.map.LocalMap

/**
 * Game model class.
 */
class Game(val map: LocalMap) {
    /**
     * Game time.
     */
    var tickTime = 0
        private set
    val player: Player = map.actors.find { it is Player } as Player? ?: map.spawnPlayer()
    private val actorsToDestroy = mutableListOf<MapActor>()

    private val actorsToSpawn = mutableListOf<MapActor>()
    private val eventsRegistrations = mutableListOf<Registration>()

    private fun onTick() {
        tickTime++
        destroyActors()
        spawnActor()
    }

    private fun destroyActors() {
        for (actor in actorsToDestroy) {
            map.actors.remove(actor)
        }
        actorsToDestroy.clear()
    }

    private fun spawnActor() {
        for (actor in actorsToSpawn) {
            map.actors.add(actor)
            actor.onBeginGame()
        }
        actorsToSpawn.clear()
    }
    /**
     * Starts the game, resetting game timer and initializing player.
     */
    fun startGame() {
        eventsRegistrations.add(EventDestroyActor.dispatcher.addHandler { actorsToDestroy.add(it.actor) })
        eventsRegistrations.add(EventSpawnActor.dispatcher.addHandler { actorsToSpawn.add(it.actor) })
        map.actors.forEach { it.onBeginGame() }
        eventsRegistrations.add(EventTick.dispatcher.addHandler(TickOrder.BEFORE_DRAW_UI) { onTick() })
    }

    /**
     * Ends the game, removing the game's tick handler from tick dispatcher.
     */
    fun endGame() {
        eventsRegistrations.forEach { it.finish() }
    }
}
