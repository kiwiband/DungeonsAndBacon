package kiwiband.dnb.actors

import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2M

class StaticActor(
    private val viewAppearance: Char,
    override val collision: Collision,
    override val pos: Vec2M = Vec2M()
) : MapActor() {
    override fun getViewAppearance(): Char = viewAppearance
    override fun collide(actor: MapActor): Collision = collision
    override fun onBeginGame() {}
    override fun onDestroy() {}
    override fun onTick() {}
}
