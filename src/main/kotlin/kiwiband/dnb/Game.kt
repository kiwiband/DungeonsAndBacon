package kiwiband.dnb

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.events.EventDestroyActor
import kiwiband.dnb.events.EventTick
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
    private var eventDestroyActorId: Int = 0
    private var eventTickId: Int = 0

    private fun onTick() {
        tickTime++
        destroyActors()
    }

    private fun destroyActors() {
        for (actor in actorsToDestroy) {
            map.actors.remove(actor)
        }
        actorsToDestroy.clear()
    }

    /**
     * Starts the game, resetting game timer and initializing player.
     */
    fun startGame() {
        eventDestroyActorId = EventDestroyActor.dispatcher.addHandler { actorsToDestroy.add(it.actor) }
        player.onBeginGame()
        map.actors.forEach { if (it !is Player) it.onBeginGame() }
        eventTickId = EventTick.dispatcher.addHandler { onTick() }
    }

    /**
     * Ends the game, removing the game's tick handler from tick dispatcher.
     */
    fun endGame() {
        EventTick.dispatcher.removeHandler(eventTickId)
        EventTick.dispatcher.removeHandler(eventDestroyActorId)
    }
}
