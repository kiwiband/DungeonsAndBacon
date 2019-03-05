package kiwiband.dnb

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.events.EventTick
import kiwiband.dnb.map.GlobalMap
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.TerminalApp

class Wall: MapActor() {
    override fun getViewAppearance() = '#'
}

class Game {
    var tickTime = 0
        private set
    var globalMap: GlobalMap = GlobalMap()
    private val map = LocalMap(MAP_WIDTH, MAP_HEIGHT)
    private val inputManager = InputManager()
    private val app = TerminalApp(map, inputManager)
    private val player = Player(map)

    fun addWall(x: Int, y: Int) {
        val wall = Wall()
        wall.position.set(x, y)
        map.actors.add(wall)
    }

    fun addSomeWalls() {
        val walls = listOf(
            3 to 3, 3 to 4, 3 to 5, 3 to 6,
            8 to 3, 8 to 4, 8 to 5, 8 to 6,
            4 to 7, 5 to 7, 6 to 7, 7 to 7)
        walls.forEach { addWall(it.first, it.second) }
    }

    init {
        // think about delete handler from dispatcher
        EventTick.dispatcher.addHandler { onTick() }
        player.position.set(Vec2M(5,5))
        map.actors.add(player)

        addSomeWalls()
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