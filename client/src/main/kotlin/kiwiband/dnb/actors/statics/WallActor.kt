package kiwiband.dnb.actors.statics

import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2M

class WallActor(pos: Vec2M) : StaticActor('▒', Collision.Block, pos) {
    override fun getType() = TYPE_ID

    companion object {
        const val TYPE_ID = "wl"
    }
}