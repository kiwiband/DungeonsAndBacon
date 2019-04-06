package kiwiband.dnb.map

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.math.Borders
import kiwiband.dnb.math.Vec2
import java.util.*

/**
 * Container for all actors on map
 */
@Suppress("unused")
class MapGrid(val width: Int, val height: Int) : Iterable<MapActor> {
    private val size = width * height
    private val data = Array<MutableList<MapActor>?>(size) { null }

    fun add(actor: MapActor) = add(actor.pos.y * width + actor.pos.x, actor)

    private fun add(index: Int, actor: MapActor) {
        data[index]?.add(actor) ?: also { data[index] = mutableListOf(actor) }
        data[index]!!.sort()
    }

    fun remove(pos: Vec2, actor: MapActor) = data[pos.y * width + pos.x]?.remove(actor)

    /**
     * Move actor from cell with [pos] to their cells
     * @return true when there is an actor in cell with [pos] which have different position
     */
    fun updateOne(pos: Vec2) : Boolean {
        val actor = get(pos.x, pos.y).find { it.pos != pos }
        if (actor != null) {
            add(actor)
            remove(pos, actor)
            return true
        }
        return false
    }

    operator fun get(pos: Vec2) = get(pos.x, pos.y)

    operator fun get(x: Int, y: Int): List<MapActor> {
        return data[y * width + x] ?: emptyList()
    }

    fun forEachCell(consumer: (List<MapActor>) -> Unit) {
        for (i in 0 until size) {
            consumer(data[i] ?: emptyList())
        }
    }

    fun forEachCell(borders: Borders, consumer: (List<MapActor>) -> Unit) {
        borders.forEach { consumer(get(it)) }
    }

    fun forEachCellIndexed(borders: Borders, consumer: (Int, Int, List<MapActor>) -> Unit) {
        borders.forEach { consumer(it.x, it.y, get(it)) }
    }

    fun forEachCellIndexed(consumer: (Int, Int, List<MapActor>) -> Unit) {
        forEachCellIndexed(Vec2() to Vec2(width, height), consumer)
    }

    private fun size(index: Int) = data[index]?.size ?: 0

    private fun isEmpty(index: Int) = data[index]?.isEmpty() != false

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
                data[index]!![innerIndex++]
            } else {
                throw NoSuchElementException()
            }
        }

    }
}
