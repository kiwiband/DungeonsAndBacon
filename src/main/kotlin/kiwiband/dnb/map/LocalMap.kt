package kiwiband.dnb.map

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.RoomType
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.StaticActor
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.math.contains
import kotlin.random.Random


@Suppress("UNUSED")
class LocalMap(val width: Int, val height: Int) {

    private val borders = Vec2M(0, 0) to Vec2M(width, height)

    private val grid: Grid = Grid(width + 1, height + 1)

    val actors = mutableListOf<MapActor>()
    val backgroundActors = mutableListOf<MapActor>()

    init {
        val dungeonGenerator = DungeonGenerator()
        dungeonGenerator.roomGenerationAttempts = 100
        dungeonGenerator.maxRoomSize = 11
        dungeonGenerator.minRoomSize = 5
        dungeonGenerator.windingChance = 0F
        dungeonGenerator.randomConnectorChance = 0F
        dungeonGenerator.wallThreshold = WALL_THRESHOLD
        dungeonGenerator.floorThreshold = FLOOR_THRESHOLD
        dungeonGenerator.corridorThreshold = CORRIDOR_THRESHOLD
        dungeonGenerator.addRoomTypes(*RoomType.DefaultRoomType.values())
        dungeonGenerator.generate(grid)
        grid.forEach { _, x, y, _ ->
            if (isWall(grid, x, y)) {
                addWall(x, y)
            } else if (grid.get(x, y) == WALL_THRESHOLD) {
                addFloor(x, y)
            }
            false
        }
    }

    private fun isWall(grid: Grid, posX: Int, posY: Int): Boolean {
        if (grid.get(posX, posY) != WALL_THRESHOLD) {
            return false
        }
        for (x in posX - 1 .. posX + 1) {
            for (y in posY - 1 .. posY + 1) {
                if (Vec2(x, y) in borders && grid.get(x, y) != WALL_THRESHOLD) {
                    return true
                }
            }
        }
        return false
    }

    private fun addWall(x: Int, y: Int) {
        val wall = StaticActor('▒', Collision.Block)
        wall.position.set(x, y)
        actors.add(wall)
    }

    private fun addFloor(x: Int, y: Int) {
        val char = if (grid.get(x, y) == WALL_THRESHOLD) '▒' else '.'
        val floor = StaticActor(char, Collision.Ignore)
        floor.position.set(x, y)
        backgroundActors.add(floor)
    }

    fun spawnPlayer(): Player {
        while (true) {
            val playerPosition = Vec2(Random.nextInt(width), Random.nextInt(height))
            if (grid.get(playerPosition.x, playerPosition.y) == FLOOR_THRESHOLD) {
                val player = Player(this, playerPosition)
                actors.add(player)
                return player
            }
        }
    }

    fun getActors(pos: Vec2M): Collection<MapActor> {
        if (pos in borders) {
            return actors.filter { it.position == pos  }
        }
        return listOf(endMap)
    }

    fun getActors(area: Pair<Vec2M, Vec2M>): Collection<MapActor> = actors.filter { it.position in area }

    companion object {

        private const val WALL_THRESHOLD = 2F
        private const val FLOOR_THRESHOLD = 1F
        private const val CORRIDOR_THRESHOLD = 0F

        private val endMap = object : MapActor() {
            override fun getViewAppearance(): Char = ' '

            override val collision = Collision.Block

            override fun collide(actor: MapActor): Collision = Collision.Block
        }
    }
}