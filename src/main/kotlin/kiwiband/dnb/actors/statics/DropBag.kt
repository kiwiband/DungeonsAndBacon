package kiwiband.dnb.actors.statics

import kiwiband.dnb.inventory.Item
import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2

class DropBag(pos: Vec2, vararg initDrop: Item) : StaticActor('δ', Collision.Overlap, pos) {
    val drop = mutableListOf(*initDrop)

    fun hasItems() = drop.isNotEmpty()

    fun getItem(): Item = drop.removeAt(drop.lastIndex)

    override fun getType(): String = TYPE_ID

    companion object {
        const val TYPE_ID = "bag"
    }
}