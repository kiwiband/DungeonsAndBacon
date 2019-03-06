package kiwiband.dnb.actors

import kiwiband.dnb.math.Collision

class StaticActor(private val viewAppearance: Char, override val collision: Collision) : MapActor() {
    override fun getViewAppearance(): Char = viewAppearance
    override fun onBeginGame() {}
    override fun onDestroy() {}
    override fun onTick() {}
}