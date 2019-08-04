package kiwiband.dnb

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.events.*
import kiwiband.dnb.map.LocalMap

/**
 * Game model class.
 */
class Game(val map: LocalMap, val eventBus: EventBus) {
    /**
     * Game time.
     */
    var tickTime: Long = 0
        private set
    private val actorsToDestroy = mutableListOf<MapActor>()

    private val actorsToSpawn = mutableListOf<MapActor>()
    private val eventsRegistrations = mutableListOf<Registration>()

    private fun onTick() {
        tickTime++
        destroyActors()
        spawnActors()
        map.updateLit()
    }

    private fun destroyActors() {
        for (actor in actorsToDestroy) {
            map.actors.remove(actor)
        }
        actorsToDestroy.clear()
    }

    private fun spawnActors() {
        for (actor in actorsToSpawn) {
            map.actors.add(actor)
            actor.onBeginGame(this)
        }
        actorsToSpawn.clear()
    }

    private fun getPlayers(): Collection<Player> {
        return map.actors.filter { it is Player }.map { it as Player }
    }

    /**
     * Starts the game, resetting game timer and initializing player.
     */
    fun startGame() {
        eventsRegistrations.add(eventBus.destroyActor.addHandler { actorsToDestroy.add(it.actor) })
        eventsRegistrations.add(eventBus.spawnActor.addHandler { actorsToSpawn.add(it.actor) })
        map.actors.forEach { it.onBeginGame(this) }
        eventsRegistrations.add(eventBus.tick.addHandler(TickOrder.BEFORE_DRAW_UI) { onTick() })
        map.updateLit()
    }

    /**
     * Ends the game, removing the game's tick handler from tick dispatcher.
     */
    fun endGame() {
        eventsRegistrations.forEach { it.finish() }
    }

    fun getOrCreatePlayer(playerId: Int): Player = map.findPlayer(playerId) ?: map.spawnPlayer(playerId)
}
