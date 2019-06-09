package kiwiband.dnb.actors.statics

import kiwiband.dnb.Game
import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.ViewAppearance
import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2

/**
 * Non-moving actor.
 * @param viewChar character used to draw the character
 * @param collision collision status
 * @param pos position
 */
abstract class StaticActor(
    private val viewChar: Char,
    override val collision: Collision,
    pos: Vec2 = Vec2()
) : MapActor() {
    init {
        super.pos.set(pos)
    }
    override fun getViewAppearance(): ViewAppearance = ViewAppearance(viewChar)
    override fun collide(actor: MapActor): Collision = collision
    override fun onBeginGame(game: Game) {}
    override fun onDestroy() {}
    override fun onTick() {}
}
