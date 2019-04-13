package kiwiband.dnb

import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.events.EventTick
import kiwiband.dnb.map.LocalMap
import org.json.JSONObject

/**
 * Game model class.
 */
class Game(val map: LocalMap) {
    /**
     * Game time.
     */
    var tickTime = 0
        private set

    private val player: Player = map.actors.find { it is Player } as Player? ?: map.spawnPlayer()

    private var eventTickId: Int = 0

    private fun onTick() {
        tickTime++
    }

    /**
     * Starts the game, resetting game timer and initializing player.
     */
    fun startGame() {
        eventTickId = EventTick.dispatcher.addHandler { onTick() }
        player.onBeginGame()
    }

    /**
     * Ends the game, removing the game's tick handler from tick dispatcher.
     */
    fun endGame() {
        EventTick.dispatcher.removeHandler(eventTickId)
    }
}
