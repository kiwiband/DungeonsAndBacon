package kiwiband.dnb.actors.statics

import kiwiband.dnb.inventory.Item
import kiwiband.dnb.math.Collision
import kiwiband.dnb.math.Vec2
import org.json.JSONArray
import org.json.JSONObject

class DropBag(pos: Vec2, vararg initDrop: Item) : StaticActor('δ', Collision.Overlap, pos) {
    val drop = mutableListOf(*initDrop)

    fun hasItems() = drop.isNotEmpty()

    fun getItem(): Item = drop.removeAt(drop.lastIndex)

    override fun getType(): String = TYPE_ID

    override fun toJSON() = super.toJSON().put("itms", JSONArray().also { arr -> drop.forEach { arr.put(it.toJSON()) } })

    companion object {
        const val TYPE_ID = "bag"

        fun fromJSON(obj: JSONObject): DropBag {
            val dropBag = DropBag(
                Vec2(obj.getInt("x"), obj.getInt("y"))
            )
            obj.getJSONArray("itms").forEach { item -> dropBag.drop.add(Item.fromJSON(item as JSONObject)) }
            return dropBag
        }
    }
}