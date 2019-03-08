package kiwiband.dnb.map

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.RoomType
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.StaticActor
import kiwiband.dnb.actors.ViewOrder
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.math.*
import kotlin.random.Random

class LocalMap(val width: Int, val height: Int) {
    private val borders = Vec2M(0, 0) to Vec2M(width, height)

    private val grid: Grid = Grid(width, height)

    val actors = MapGrid(width, height)

    var player: Player? = null

    init {
        val dungeonGenerator = DungeonGenerator()
        dungeonGenerator.roomGenerationAttempts = 100
        dungeonGenerator.maxRoomSize = 11
        dungeonGenerator.minRoomSize = 5
        dungeonGenerator.windingChance = 0F
        dungeonGenerator.randomConnectorChance = 0.01f
        dungeonGenerator.wallThreshold = WALL_THRESHOLD
        dungeonGenerator.floorThreshold = FLOOR_THRESHOLD
        dungeonGenerator.corridorThreshold = CORRIDOR_THRESHOLD
        dungeonGenerator.addRoomTypes(*RoomType.DefaultRoomType.values())
        dungeonGenerator.generate(grid)
        grid.forEach { _, x, y, value ->
            if (isWall(grid, x, y)) {
                addWall(x, y)
            } else if (value == WALL_THRESHOLD) {
                addStaticBackground(x, y, '▒')
            }
            false
        }
    }

    private fun isWall(grid: Grid, x: Int, y: Int): Boolean {
        if (grid.get(x, y) != WALL_THRESHOLD) {
            return false
        }
        val area = Borders(x - 1, y - 1, x + 2, y + 2).fitIn(borders)
        return area.any { grid.get(it.x, it.y) != WALL_THRESHOLD }
    }

    private fun addWall(x: Int, y: Int) {
        val wall = StaticActor('▒', Collision.Block)
        wall.pos.set(x, y)
        actors.add(wall)
    }

    private fun addStaticBackground(x: Int, y: Int, char: Char) {
        val floor = StaticActor(char, Collision.Ignore)
        floor.viewOrder = ViewOrder.Background
        floor.pos.set(x, y)
        actors.add(floor)
    }

    fun spawnPlayer(): Player {
        while (true) {
            val playerPosition = Vec2(Random.nextInt(width), Random.nextInt(height))
            if (grid.get(playerPosition.x, playerPosition.y) == FLOOR_THRESHOLD) {
                player = Player(this, playerPosition)
                actors.add(player!!)
                return player!!
            }
        }
    }

    fun removePlayer() {
        player = null
    }

    fun getActors(pos: Vec2M): Collection<MapActor> = if (pos in borders) actors[pos] else listOf(endMap)

    companion object {

        private const val WALL_THRESHOLD = 2F
        private const val FLOOR_THRESHOLD = 1F
        private const val CORRIDOR_THRESHOLD = 0F

        private val endMap = StaticActor('~', Collision.Block)
    }
}
