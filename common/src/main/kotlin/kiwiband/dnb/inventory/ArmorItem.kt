package kiwiband.dnb.inventory

import kiwiband.dnb.actors.creatures.Creature
import kiwiband.dnb.ASCIIART
import org.json.JSONObject

class ArmorItem(
    val id: Int,
    val defence: Int,
    override val name: String,
    override val description: String,
    override val icon: String = ASCIIART.ARMOR
) : EquipmentItem() {

    override val slot = EquipmentSlot.Armor

    override fun onEquip(owner: Creature) {
        owner.status.armorDefence += defence
    }

    override fun onUnequip(owner: Creature) {
        owner.status.armorDefence -= defence
    }

    override fun clone() = ArmorItem(id, defence, name, description, icon)

    override fun toJSON(): JSONObject = super.toJSON().put("t", "a").put("id", id)
}