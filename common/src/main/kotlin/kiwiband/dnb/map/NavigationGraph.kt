package kiwiband.dnb.map

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.creatures.Creature
import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2
import java.util.*

class NavigationGraph(val map: MapGrid) {
    private val marks: IntArray = IntArray(map.height * map.width) { -1 }
    private val dists: IntArray = IntArray(map.height * map.width) { -1 }
    private val deque = ArrayDeque<MapCell>()


    fun pathToNearest(start: Vec2, maxDistance: Int, f: (Collection<MapActor>) -> Boolean): MutableList<Vec2>? {
        val nearestCell = bfs(start, maxDistance) { cell -> f(cell.getActors()) }
        return nearestCell?.let { findPath(it.getId()) }
    }

    fun nextToNearest(start: Vec2, maxDistance: Int, f: (Collection<MapActor>) -> Boolean): Vec2? {
        return bfs(start, maxDistance + 1) { cell ->
            cell.neighbours().any { neighbour ->
                val dist = dists[neighbour.getId()]
                0 < dist && dist < dists[cell.getId()] && f(neighbour.getActors())
            }
        }?.toVec2()
    }

    fun nearest(start: Vec2, maxDistance: Int, f: (Collection<MapActor>) -> Boolean): Vec2? {
        return bfs(start, maxDistance) { cell -> f(cell.getActors()) }?.toVec2()
    }

    private fun bfs(start: Vec2, maxDistance: Int, f: (MapCell) -> Boolean): MapCell? {
        marks.fill(-1)
        dists.fill(-1)
        deque.clear()

        val startCell = MapCell(start.x, start.y)
        marks[startCell.getId()] = startCell.getId()
        dists[startCell.getId()] = 0
        deque.addLast(startCell)
        while (deque.isNotEmpty()) {
            val cell = deque.pollFirst()
            val cellDist = start.distance(cell.x, cell.y)
            if (cellDist > maxDistance) return null
            if (f(cell)) {
                return cell
            }
            val id = cell.getId()
            val dist = dists[id]
            cell.neighbours().shuffled().forEach {
                val itId = it.getId()
                if (marks[itId] < 0) {
                    deque.addLast(it)
                    marks[itId] = id
                    dists[itId] = dist + 1
                }
            }
        }
        return null
    }

    private fun findPath(startId: Int): MutableList<Vec2> {
        val path = mutableListOf<Vec2>()
        var id = startId
        while (marks[id] != id) {
            path.add(toVec2(id))
            id = marks[id]
        }
        return path
    }

    private fun toVec2(id: Int): Vec2 {
        val y: Int = id / map.width
        return Vec2(id - y * map.width, y)
    }

    inner class MapCell(val x: Int, val y: Int) {
        fun getActors() = map[x, y].actors

        fun getId() = y * map.width + x

        fun neighbours(): Iterable<MapCell> {
            MapCellList.clear()
            if (checkCell(x, y + 1)) MapCellList.add(MapCell(x, y + 1))
            if (checkCell(x, y - 1)) MapCellList.add(MapCell(x, y - 1))
            if (checkCell(x + 1, y)) MapCellList.add(MapCell(x + 1, y))
            if (checkCell(x - 1, y)) MapCellList.add(MapCell(x - 1, y))
            return MapCellList
        }

        fun toVec2(): Vec2 {
            return Vec2(x, y)
        }

        private fun checkCell(x: Int, y: Int): Boolean =
            map[x, y].actors.all { it.collision != Collision.Block || it is Creature }
    }

    companion object {
        val MapCellList: MutableList<MapCell> = mutableListOf()
    }
}
