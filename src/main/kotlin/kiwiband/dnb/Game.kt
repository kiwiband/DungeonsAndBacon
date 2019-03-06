package kiwiband.dnb

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.events.EventTick
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2M

class Wall: MapActor() {
    override fun getViewAppearance() = '#'
}

class Game {
    var tickTime = 0
        private set
    val map = LocalMap(MAP_WIDTH, MAP_HEIGHT)
    val player = Player(map)

    // TODO: delete
    fun addWall(x: Int, y: Int) {
        val wall = Wall()
        wall.position.set(x, y)
        map.actors.add(wall)
    }

    // TODO: delete
    fun addSomeWalls() {
        val walls = listOf(
            3 to 3, 3 to 4, 3 to 5, 3 to 6,
            8 to 3, 8 to 4, 8 to 5, 8 to 6,
            4 to 7, 5 to 7, 6 to 7, 7 to 7)
        walls.forEach { addWall(it.first, it.second) }
    }

    private var eventTickId: Int = 0

    init {
        // think about delete handler from dispatcher
        player.position.set(Vec2M(5,5))
        map.actors.add(player)

        addSomeWalls()
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

    companion object {
        const val MAP_WIDTH = 88
        const val MAP_HEIGHT = 32
    }
}