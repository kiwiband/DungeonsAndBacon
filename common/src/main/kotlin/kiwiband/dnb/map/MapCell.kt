package kiwiband.dnb.map

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.math.VisibilityLevel.Block
import kiwiband.dnb.math.VisibilityLevel.Pass

class MapCell() {
    val actors = mutableListOf<MapActor>()

    val visibilityLevel
        get() = if (actors.all { it.visibilityLevel == Pass }) Pass else Block

    var explored = false

    var lit = false

    val count
        get() = actors.size

    fun lit() {
        lit = true
        explored = true
    }

    constructor(mapActor: MapActor) : this() {
        add(mapActor)
    }

    fun isEmpty() = actors.isEmpty()

    fun add(actor: MapActor) = actors.add(actor)

    fun sort() = actors.sort()

    fun remove(actor: MapActor) = actors.remove(actor)
}
