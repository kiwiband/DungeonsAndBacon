package kiwiband.dnb

import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.events.EventTick
import kiwiband.dnb.map.GlobalMap
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.TerminalApp

class Game {
    var tickTime = 0
        private set
    var globalMap: GlobalMap = GlobalMap()
    private val map = LocalMap(MAP_WIDTH, MAP_HEIGHT)
    private val inputManager = InputManager()
    private val app = TerminalApp(map, inputManager)
    private val player = Player(map)

    init {
        // think about delete handler from dispatcher
        EventTick.dispatcher.addHandler { onTick() }
        player.position.set(Vec2M(5,5))
        map.actors.add(player)

//        val p = Player(map)
//        p.position.set(7, 7)
//        map.actors.add(p)
    }

    private fun onTick() {
        tickTime++
        app.drawScene()
    }

    fun startGame() {
        player.onBeginGame()
        app.start()
    }

    companion object {
        const val MAP_WIDTH = 48
        const val MAP_HEIGHT = 22
    }
}

fun main() {
    Game().startGame()
}