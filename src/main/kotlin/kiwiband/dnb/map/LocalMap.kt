package kiwiband.dnb.map

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.RoomType
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.StaticActor
import kiwiband.dnb.actors.ViewOrder
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.math.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

// todo get rid of the kostyl' with 'generated' param
class LocalMap(val width: Int, val height: Int) {

    private val borders = Vec2M(0, 0) to Vec2M(width, height)

    private val grid: Grid = Grid(width, height)

    val actors = MapGrid(width, height)

    var player: Player? = null

    fun toJSON(): JSONObject {
        val actorsArray = JSONArray()
        actors.forEach { actor ->
            val x = actor.pos.x
            val y = actor.pos.y
            val type = when {
                actor == player -> "plyr"
                actor.getViewAppearance() == WALL_APPEARANCE -> "wall"
                else -> "bgnd"
            }
            actorsArray.put(
                JSONObject()
                    .put("x", x)
                    .put("y", y)
                    .put("type", type)
            )
        }
        return JSONObject()
            .put("width", width)
            .put("height", height)
            .put("actors", actorsArray)
    }

    private fun addWall(x: Int, y: Int) {
        val wall = StaticActor(WALL_APPEARANCE, Collision.Block)
        wall.pos.set(x, y)
        actors.add(wall)
    }

    private fun addStaticBackground(x: Int, y: Int, char: Char) {
        val floor = StaticActor(char, Collision.Ignore)
        floor.viewOrder = ViewOrder.Background
        floor.pos.set(x, y)
        actors.add(floor)
    }

    fun addPlayer(x: Int, y: Int) {
        val playerPosition = Vec2(x, y)
        player = Player(this, playerPosition)
        actors.add(player!!)
    }

    fun spawnPlayer(): Player {
        if (player != null) return player!!
        while (true) {
            val x = Random.nextInt(grid.width)
            val y = Random.nextInt(grid.height)
            if (grid.get(x, y) == FLOOR_THRESHOLD) {
                addPlayer(x, y)
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

        private const val BKGND_APPEARANCE = '▒'
        private const val WALL_APPEARANCE = '▒'

        private val endMap = StaticActor('~', Collision.Block)

        fun generateMap(width: Int, height: Int): LocalMap {
            val map = LocalMap(width, height)
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
            dungeonGenerator.generate(map.grid)
            map.grid.forEach { _, x, y, value ->
                if (isWall(map, x, y)) {
                    map.addWall(x, y)
                } else if (value == WALL_THRESHOLD) {
                    map.addStaticBackground(x, y, BKGND_APPEARANCE)
                }
                false
            }
            return map
        }

        fun loadMap(mapData: JSONObject): LocalMap {
            val map = LocalMap(mapData.getInt("width"), mapData.getInt("height"))

            map.grid.fill(FLOOR_THRESHOLD)
            mapData.getJSONArray("actors").forEach {
                val actorObject = it as JSONObject
                val x = actorObject.getInt("x")
                val y = actorObject.getInt("y")
                when (actorObject.getString("type")) {
                    "wall" -> map.addWall(x, y)
                    "bgnd" -> map.addStaticBackground(x, y, BKGND_APPEARANCE)
                    "plyr" -> map.addPlayer(x, y)
                }
                map.grid.set(x, y, WALL_THRESHOLD)
            }
            return map
        }

        private fun isWall(map: LocalMap, x: Int, y: Int): Boolean {
            if (map.grid.get(x, y) != WALL_THRESHOLD) {
                return false
            }
            val area = Borders(x - 1, y - 1, x + 2, y + 2).fitIn(map.borders)
            return area.any { map.grid.get(it.x, it.y) != WALL_THRESHOLD }
        }
    }
}
