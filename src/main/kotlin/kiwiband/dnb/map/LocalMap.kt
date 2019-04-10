package kiwiband.dnb.map

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.RoomType
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.StaticActor
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.math.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

/**
 * Class for game location
 */
class LocalMap(val width: Int, val height: Int) {

    private val borders = Vec2(0, 0) to Vec2(width, height)

    private val grid: Grid = Grid(width, height)

    val actors = MapGrid(width, height)

    fun toJSON(): JSONObject {
        val actorsArray = JSONArray()
        actors.forEach { actor ->
            val x = actor.pos.x
            val y = actor.pos.y
            val type = when {
                actor is Player -> "plyr"
                actor.getViewAppearance() == WALL_APPEARANCE -> "wl"
                else -> "none"
            }
            actorsArray.put(
                JSONObject()
                    .put("x", x)
                    .put("y", y)
                    .put("t", type)
            )
        }
        return JSONObject()
            .put("width", width)
            .put("height", height)
            .put("actors", actorsArray)
    }

    private fun addWall(x: Int, y: Int) {
        val wall = StaticActor(WALL_APPEARANCE, Collision.Block, Vec2M(x, y))
        actors.add(wall)
    }

    private fun addPlayer(x: Int, y: Int): Player {
        val player = Player(this, Vec2M(x, y))
        actors.add(player)
        return player
    }

    fun spawnPlayer(): Player {
        while (true) {
            val x = Random.nextInt(grid.width)
            val y = Random.nextInt(grid.height)
            if (grid.get(x, y) == FLOOR_THRESHOLD) {
                return addPlayer(x, y)
            }
        }
    }

    fun getActors(pos: Vec2): Collection<MapActor> = if (pos in borders) actors[pos] else listOf(endMap)

    companion object {

        private const val WALL_THRESHOLD = 2F
        private const val FLOOR_THRESHOLD = 1F
        private const val CORRIDOR_THRESHOLD = 0F

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
                if (value == WALL_THRESHOLD) {
                    map.addWall(x, y)
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
                when (actorObject.getString("t")) {
                    "wl" -> map.addWall(x, y)
                    "plyr" -> map.addPlayer(x, y)
                }
                map.grid.set(x, y, WALL_THRESHOLD)
            }
            return map
        }
    }
}
