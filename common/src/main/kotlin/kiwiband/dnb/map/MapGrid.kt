package kiwiband.dnb.map

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.math.Borders
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.math.VisibilityLevel
import java.util.*

/**
 * Container for all actors on map
 */
class MapGrid(val width: Int, val height: Int) : Iterable<MapActor> {
    private val size = width * height
    private val data = Array(size) { MapCell() }

    /**
     * Adds an actor to a map.
     * @param actor actor to add
     */
    fun add(actor: MapActor) = add(actor.pos.y * width + actor.pos.x, actor)

    private fun add(index: Int, actor: MapActor) {
        data[index].add(actor)
        data[index].sort()
    }

    /**
     * Removes a map actor from a given position in a map.
     * @param pos position to remove
     * @param actor actor to remove
     */
    private fun remove(pos: Vec2, actor: MapActor) = data[pos.y * width + pos.x].remove(actor)

    /**
     * Removes a map actor from a map.
     * @param actor actor to remove
     */
    fun remove(actor: MapActor) = remove(actor.pos, actor)

    /**
     * Moves an actor at [pos] to its actual cell
     * @return true if at least one actor is updated
     */
    fun updateOne(pos: Vec2) : Boolean {
        val actor = get(pos.x, pos.y).actors.find { it.pos != pos }
        if (actor != null) {
            add(actor)
            remove(pos, actor)
            return true
        }
        return false
    }

    operator fun get(pos: Vec2) = get(pos.x, pos.y)

    operator fun get(x: Int, y: Int): MapCell {
        return data[y * width + x]
    }

    fun getSafe(x: Int, y: Int): MapCell? {
        val ind = y * width + x
        return if ((x in 0 until width) && (y in 0 until height)) data[ind] else null
    }

    @Suppress("unused")
    fun forEachCell(consumer: (MapCell) -> Unit) {
        for (i in 0 until size) {
            consumer(data[i])
        }
    }

    @Suppress("unused")
    fun forEachCell(borders: Borders, consumer: (MapCell) -> Unit) {
        borders.forEach { consumer(get(it)) }
    }

    fun forEachCellIndexed(borders: Borders, consumer: (Int, Int, MapCell?) -> Unit) {
        borders.forEach { consumer(it.x, it.y, getSafe(it.x, it.y)) }
    }

    @Suppress("unused")
    fun forEachCellIndexed(consumer: (Int, Int, MapCell) -> Unit) {
        (Vec2() to Vec2(width, height)).forEach {
            consumer(it.x, it.y, get(it))
        }
    }

    fun unlit() = forEachCell { it.lit = false } // TODO optimize

    fun fov(pos: Vec2, radius: Int) {
        for (i in -radius..radius) {
            for (j in -radius..radius) {
                if(i * i + j * j < radius * radius)
                    los(pos.x, pos.y, pos.x + i, pos.y + j)
            }
        }
    }

    private fun los(x0: Int, y0: Int, x1: Int, y1: Int) {
        val sx = if (x0 < x1) 1 else -1
        val sy = if (y0 < y1) 1 else -1
        val dx = x1 - x0
        val dy = y1 - y0
        val dist = Math.sqrt((dx * dx + dy * dy).toDouble())

        var xnext = x0
        var ynext = y0

        val destination = getSafe(x1, y1)

        if (destination == null || destination.lit) {
            return
        }

        while (xnext != x1 || ynext != y1) {
            val cell = getSafe(xnext, ynext)
            cell?.lit()
            if (cell == null || cell.visibilityLevel == VisibilityLevel.Block) {
                return
            }

            // Line-to-point distance formula < 0.5
            when {
                Math.abs(dy * (xnext - x0 + sx) - dx * (ynext - y0)) / dist < 0.5f -> xnext += sx
                Math.abs(dy * (xnext - x0) - dx * (ynext - y0 + sy)) / dist < 0.5f -> ynext += sy
                else -> {
                    xnext += sx
                    ynext += sy
                }
            }
        }

        destination.lit()
    }

    private fun size(index: Int) = data[index].count

    private fun isEmpty(index: Int): Boolean = data[index].isEmpty()

    override fun iterator(): Iterator<MapActor> = AllActorsIterator()

    inner class AllActorsIterator : Iterator<MapActor> {
        private var index = 0
        private var innerIndex = 0
        private var ready = false

        override fun hasNext(): Boolean {
            return if (index < size && size(index) <= innerIndex) {
                innerIndex = 0
                index++
                while (index < size && isEmpty(index)) {
                    index++
                }
                ready = index < size
                index < size
            } else {
                ready = true
                true
            }
        }

        override fun next(): MapActor {
            return if (ready || hasNext()) {
                ready = false
                data[index].actors[innerIndex++]
            } else {
                throw NoSuchElementException()
            }
        }

    }
}
