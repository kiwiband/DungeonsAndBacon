package kiwiband.dnb.inventory

import kiwiband.dnb.JSONSerializable
import kiwiband.dnb.actors.creatures.Creature
import org.json.JSONArray
import org.json.JSONObject

class Inventory(val capacity: Int) : JSONSerializable {
    private val items = mutableListOf<Item>()

    fun add(item: Item): Boolean {
        if (hasSpace()) {
            items.add(item)
            return true
        }
        return false
    }

    fun items(): List<Item> = items

    fun get(i: Int): Item = items[i]

    fun remove(item: Item) = items.remove(item)

    fun getSize(): Int = items.size
    fun isFull(): Boolean = !hasSpace()
    fun hasSpace(): Boolean = items.size < capacity
    fun isEmpty(): Boolean = items.size == 0

    override fun toJSON(): JSONObject = JSONObject()
        .put("cap", capacity)
        .put("itms", JSONArray().also { arr -> items.forEach { arr.put(it.toJSON()) } })

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