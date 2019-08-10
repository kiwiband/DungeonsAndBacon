package kiwiband.dnb.server

import kiwiband.dnb.Game
import kiwiband.dnb.Settings
import kiwiband.dnb.events.EventBus
import kiwiband.dnb.map.LocalMap
import java.util.concurrent.locks.ReentrantLock

/**
 * An instance of a game on server
 */
class GameSession {

    /**
     * NOTE: Access this field between lock() and unlock() calls
     */
    val game: Game = Game(LocalMap.generateMap(Settings.mapWidth, Settings.mapHeight), EventBus())
    private val lock = ReentrantLock()
    private val playerIds = HashSet<String>()

    init {
        game.startGame()
    }

    fun lock() = lock.lock()

    fun unlock()  = lock.unlock()

    /**
     * Add a new player with the given id and spawn it on the map
     *
     * NOTE: this method is thread unsafe! Use it between lock() and unlock() calls
     */
    fun addNewPlayer(id: String) {
        val player = game.map.spawnPlayer(id)
        player.onBeginGame(game)
    }

    /**
     * Remove a player with the given id from the map
     *
     * NOTE: this method is thread unsafe! Use it between lock() and unlock() calls
     */
    fun removePlayer(id: String) {
        synchronized(playerIds) {
            playerIds.remove(id)
        }
        game.map.findPlayer(id)?.let { game.map.actors.remove(it) }
    }
}