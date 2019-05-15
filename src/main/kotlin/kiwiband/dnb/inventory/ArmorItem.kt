package kiwiband.dnb.inventory

import kiwiband.dnb.actors.creatures.Creature
import kiwiband.dnb.ui.ASCIIART
import org.json.JSONObject

class ArmorItem(
    val id: Int,
    val defence: Int,
    private val name: String,
    private val description: String,
    private val icon:String = ASCIIART.ARMOR
) : EquipmentItem() {
    override fun getName() = name

    override fun getIcon() = icon

    override fun getSlot() = EquipmentSlot.Armor

    override fun getDescription() = description

    override fun onEquip(owner: Creature) {
        owner.status.armorDefence += defence
    }

    override fun onUnequip(owner: Creature) {
        owner.status.armorDefence -= defence
    }

    override fun clone() = ArmorItem(id, defence, name, description, icon)

    override fun toJSON(): JSONObject = super.toJSON().put("t", "a").put("id", id)
}