package kiwiband.dnb.actors.statics

import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.math.VisibilityLevel

class WallActor(pos: Vec2M) : StaticActor('▒', Collision.Block, pos) {
    override fun getType() = TYPE_ID

    override var visibilityLevel = VisibilityLevel.Block
    override val litIfExplored = true

    companion object {
        const val TYPE_ID = "wl"
    }
}