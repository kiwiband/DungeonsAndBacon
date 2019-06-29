package kiwiband.dnb.inventory

import kiwiband.dnb.JSONSerializable
import kiwiband.dnb.actors.creatures.Creature
import org.json.JSONObject

abstract class Item : JSONSerializable {
    abstract val name: String
    abstract val icon: String
    abstract val description: String

    abstract fun clone(): Item

    companion object {
        fun fromJSON(obj: JSONObject, owner: Creature? = null): Item {
            val id = obj.getInt("id")
            return when (obj.getString("t")) {
                "a" -> ArmorItemsEnum.allArmors[id].get()
                "w" -> WeaponItemsEnum.allWeapons[id].get()
                else -> SpecialItems.dummy
            }.also {
                if (it is EquipmentItem && obj.getBoolean("eqp") && owner != null) {
                    it.equipBy(owner)
                } }
        }
    }
}