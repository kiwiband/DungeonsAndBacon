package kiwiband.dnb.actors

import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.math.Vec2M

/**
 * Non-moving actor.
 * @param viewAppearance character used to draw the character
 * @param collision collision status
 * @param pos position
 */
class StaticActor(
    private val viewAppearance: Char,
    override val collision: Collision,
    pos: Vec2 = Vec2()
) : MapActor() {
    init {
        super.pos.set(pos)
    }
    override fun getViewAppearance(): Char = viewAppearance
    override fun collide(actor: MapActor): Collision = collision
    override fun onBeginGame() {}
    override fun onDestroy() {}
    override fun onTick() {}
}
