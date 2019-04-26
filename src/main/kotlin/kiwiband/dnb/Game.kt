package kiwiband.dnb

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.events.EventDestroyActor
import kiwiband.dnb.events.EventSpawnActor
import kiwiband.dnb.events.EventTick
import kiwiband.dnb.events.Registration
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
        player.onBeginGame()
        map.actors.forEach { if (it !is Player) it.onBeginGame() }
        eventsRegistrations.add(EventTick.dispatcher.addHandler { onTick() })
    }

    /**
     * Ends the game, removing the game's tick handler from tick dispatcher.
     */
    fun endGame() {
        eventsRegistrations.forEach { it.finish() }
    }
}
