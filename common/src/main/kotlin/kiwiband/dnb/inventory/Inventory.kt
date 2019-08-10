package kiwiband.dnb.inventory

import kiwiband.dnb.JSONSerializable
import kiwiband.dnb.actors.creatures.Creature
import org.json.JSONArray
import org.json.JSONObject

class Inventory(val capacity: Int) : JSONSerializable {
    private val myItems = mutableListOf<Item>()

    fun add(item: Item): Boolean {
        if (hasSpace()) {
            myItems.add(item)
            return true
        }
        return false
    }

    val items: List<Item>
        get() = myItems

    operator fun get(i: Int): Item? {
        return if (i < myItems.size) myItems[i] else null
    }

    fun remove(item: Item) = myItems.remove(item)

    val size: Int
        get() = myItems.size

    fun isFull(): Boolean = !hasSpace()
    fun hasSpace(): Boolean = myItems.size < capacity
    fun isEmpty(): Boolean = myItems.size == 0

    override fun toJSON(): JSONObject = JSONObject()
        .put("cap", capacity)
        .put("itms", JSONArray().also { arr -> myItems.forEach { arr.put(it.toJSON()) } })

    companion object {
        fun fromJSON(obj: JSONObject, owner: Creature? = null): Inventory {
            val inv = Inventory(obj.getInt("cap"))
            for (item in obj.getJSONArray("itms")) {
                inv.add(Item.fromJSON(item as JSONObject, owner))
            }
            return inv
        }
    }
}