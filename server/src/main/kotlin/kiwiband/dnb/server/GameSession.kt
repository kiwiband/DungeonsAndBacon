package kiwiband.dnb.server

import kiwiband.dnb.Game
import kiwiband.dnb.map.LocalMap
import java.util.concurrent.locks.ReentrantLock

class GameSession {

    val game: Game = Game(LocalMap.generateMap(MAP_WIDTH, MAP_HEIGHT))

    init {
        game.startGame()
    }

    fun addNewPlayer(id: Int) {
        val player = game.map.spawnPlayer(id)
        player.onBeginGame()
    }

    fun removePlayer(id: Int) {
        game.map.findPlayer(id)?.let { game.map.actors.remove(it) }
    }

    companion object {
        private const val MAP_WIDTH = 80
        private const val MAP_HEIGHT = 24
    }
}