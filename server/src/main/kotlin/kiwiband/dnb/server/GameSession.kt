package kiwiband.dnb.server

import kiwiband.dnb.Game
import kiwiband.dnb.map.LocalMap

class GameSession(map: LocalMap) {
    val game: Game = Game(map)
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
}