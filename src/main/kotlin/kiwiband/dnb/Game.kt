package kiwiband.dnb

import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.events.EventTick
import kiwiband.dnb.map.LocalMap
import org.json.JSONObject

class Game(val map: LocalMap) {
    var tickTime = 0
        private set

    val player: Player

    private var eventTickId: Int = 0

    init {
        // think about delete handler from dispatcher
        player = map.spawnPlayer()
    }

    private fun onTick() {
        tickTime++
    }

    fun startGame() {
        eventTickId = EventTick.dispatcher.addHandler { onTick() }
        player.onBeginGame()
    }

    fun endGame() {
        EventTick.dispatcher.removeHandler(eventTickId)
    }
}
