package kiwiband.dnb.map

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.RoomType
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator
import kiwiband.dnb.JSONSerializable
import kiwiband.dnb.Settings

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.MapActorFactory
import kiwiband.dnb.actors.statics.StaticActor
import kiwiband.dnb.actors.creatures.MobFactory
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.actors.creatures.status.CreatureStatus
import kiwiband.dnb.actors.statics.WallActor
import kiwiband.dnb.math.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

/**
 * Class for game map.
 */
class LocalMap(val width: Int, val height: Int) : JSONSerializable {

    private val borders = Vec2(0, 0) to Vec2(width, height)

    private val grid: Grid = Grid(width, height)

    val actors = MapGrid(width, height)
    val navigationGraph = NavigationGraph(actors)

    /**
     * Serializes the map to JSON.
     * @return JSON object
     */
    override fun toJSON(): JSONObject {
        val actorsArray = JSONArray()
        actors.forEach { actor -> actorsArray.put(actor.toJSON()) }
        return JSONObject()
            .put("width", width)
            .put("height", height)
            .put("actors", actorsArray)
    }

    /**
     * @inherit
     *
     * Generate a string with JSON representation of the map
     * @return Json string
     */
    override fun toString(): String {
        return toJSON().toString()
    }

    fun spawnPlayer(id: Int): Player {
        while (true) {
            val x = Random.nextInt(grid.width)
            val y = Random.nextInt(grid.height)
            if (grid.get(x, y) == FLOOR_THRESHOLD) {
                val player = Player(this, Vec2M(x, y), CreatureStatus.generateDefault(), id)
                actors.add(player)
                return player
            }
        }
    }


    fun spawnMob(n: Int) {
        for (i in 0 until n) {
            while (true) {
                val x = Random.nextInt(grid.width)
                val y = Random.nextInt(grid.height)
                if (grid.get(x, y) == FLOOR_THRESHOLD) {
                    val mob = MobFactory.createMob(this, Vec2M(x, y), CreatureStatus.generateRandom())
                    actors.add(mob)
                    break
                }
            }
        }
    }

    fun findPlayer(id: Int) = actors.find { it is Player && it.playerId == id} as Player?

    fun getActors(pos: Vec2): Collection<MapActor> = if (pos in borders) actors[pos] else listOf(endMap)

    companion object {

        private const val WALL_THRESHOLD = 2F
        private const val FLOOR_THRESHOLD = 1F
        private const val CORRIDOR_THRESHOLD = 0F

        private val endMap = object : StaticActor('~', Collision.Block, Vec2M()) {
            override fun getType() = "none"
        }

        /**
         * Generates a map.
         * @param width map width
         * @param height map height
         * @return created map
         */
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
                    map.actors.add(WallActor(Vec2M(x, y)))
                }
                false
            }
            map.spawnMob(Settings.mobsCount)
            return map
        }

        /**
         * Deserializes a map from JSON.
         * @param mapData JSON map
         * @return deserialized map.
         */
        fun loadMap(mapData: JSONObject): LocalMap {
            val map = LocalMap(mapData.getInt("width"), mapData.getInt("height"))

            map.grid.fill(FLOOR_THRESHOLD)
            mapData.getJSONArray("actors").forEach { json ->
                MapActorFactory.createMapActor(map, json as JSONObject)?.also {
                    map.actors.add(it)
                    if (it is WallActor) {
                        map.grid.set(it.pos.x, it.pos.y, WALL_THRESHOLD)
                    }
                }
            }
            return map
        }
    }
}
