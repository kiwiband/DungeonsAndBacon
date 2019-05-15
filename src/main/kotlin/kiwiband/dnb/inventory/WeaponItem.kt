package kiwiband.dnb.inventory

import kiwiband.dnb.actors.creatures.Creature
import kiwiband.dnb.ui.ASCIIART
import org.json.JSONObject

class WeaponItem(
    val id: Int,
    val damage: Int,
    private val name: String,
    private val description: String,
    private val icon:String = ASCIIART.SWORD
) : EquipmentItem() {
    override fun getName() = name

    override fun getIcon() = icon

    override fun getDescription() = description

    override fun getSlot() = EquipmentSlot.Weapon

    override fun onEquip(owner: Creature) {
        owner.status.weaponAttack += damage;
    }

    override fun onUnequip(owner: Creature) {
        owner.status.weaponAttack -= damage
    }

    override fun clone() = WeaponItem(id, damage, name, description, icon)

    override fun toJSON(): JSONObject = super.toJSON().put("t", "w").put("id", id)
}
