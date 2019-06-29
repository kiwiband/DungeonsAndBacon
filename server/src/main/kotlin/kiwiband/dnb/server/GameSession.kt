package kiwiband.dnb.server

import kiwiband.dnb.Game
import kiwiband.dnb.Settings
import kiwiband.dnb.events.EventBus
import kiwiband.dnb.map.LocalMap

/**
 * An instance of a game on server
 */
class GameSession {

    val game: Game = Game(LocalMap.generateMap(Settings.mapWidth, Settings.mapHeight), EventBus())
    private val playerIds = HashSet<Int>()

    init {
        game.startGame()
    }

    /**
     * Find the MEX of the existing player ids
     */
    fun getFreePlayerId(): Int {
        var result = 0
        synchronized(playerIds) {
            while (playerIds.contains(result)) {
                ++result
            }
            playerIds.add(result)
        }
        return result
    }

    /**
     * Add a new player with the given id and spawn it on the map
     */
    fun addNewPlayer(id: Int) {
        val player = game.map.spawnPlayer(id)
        player.onBeginGame(game)
    }

    /**
     * Remove a player with the given id from the map
     */
    fun removePlayer(id: Int) {
        synchronized(playerIds) {
            playerIds.remove(id)
        }
        game.map.findPlayer(id)?.let { game.map.actors.remove(it) }
    }
}