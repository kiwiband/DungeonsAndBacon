package kiwiband.dnb.server

import kiwiband.dnb.Game
import kiwiband.dnb.Settings
import kiwiband.dnb.events.EventBus
import kiwiband.dnb.map.LocalMap
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock

/**
 * An instance of a game on server
 */
class GameSession {

    val game: Game = Game(LocalMap.generateMap(Settings.mapWidth, Settings.mapHeight), EventBus())
    val currentMap = AtomicReference(game.map.toString())
    private val lock = ReentrantLock()
    private val playerIds = HashSet<String>()

    init {
        game.startGame()
    }

    fun lock() = lock.lock()

    fun unlock()  = lock.unlock()

    /**
     * Add a new player with the given id and spawn it on the map
     */
    fun addNewPlayer(id: String) {
        val player = game.map.spawnPlayer(id)
        player.onBeginGame(game)
        currentMap.set(game.map.toString())
    }

    /**
     * Remove a player with the given id from the map
     */
    fun removePlayer(id: String) {
        synchronized(playerIds) {
            playerIds.remove(id)
        }
        game.map.findPlayer(id)?.let { game.map.actors.remove(it) }
    }
}