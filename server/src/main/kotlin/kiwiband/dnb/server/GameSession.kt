package kiwiband.dnb.server

import kiwiband.dnb.Game
import kiwiband.dnb.Settings
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.events.EventBus
import kiwiband.dnb.map.LocalMap

/**
 * An instance of a game on server
 */
class GameSession {

    val game: Game = Game(LocalMap.generateMap(Settings.mapWidth, Settings.mapHeight), EventBus())

    init {
        game.startGame()
    }

    /**
     * Find a minimal non used id from all players on the map
     */
    fun getFreePlayerId(): Int {
        var result = 0
        val ids = game.map.actors.filter { it is Player }.map { (it as Player).playerId }.sorted()
        for (id in ids) {
            if (id == result) {
                result++
            } else {
                break
            }
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
        game.map.findPlayer(id)?.let { game.map.actors.remove(it) }
    }

    companion object {
        private const val MAP_WIDTH = 80
        private const val MAP_HEIGHT = 24
    }
}