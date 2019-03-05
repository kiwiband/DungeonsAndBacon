package kiwiband.dnb.map

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.math.contains

class LocalMap(val x: Int, val y: Int) {
    private val borders = Vec2M(0, 0) to Vec2M(x, y)

    val actors = mutableListOf<MapActor>()

    fun getActors(pos: Vec2M): Collection<MapActor> {
        if (pos in borders) {
            return actors.filter { it.position == pos  }
        }
        return listOf(endMap)
    }

    companion object {
        private val endMap = object : MapActor() {
            override fun getViewAppearance(): Char = ' '

            override val collision = Collision.Block

            override fun collide(actor: MapActor): Collision = Collision.Block
        }
    }
}