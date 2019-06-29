package kiwiband.dnb.inventory

import kiwiband.dnb.actors.creatures.Creature
import kiwiband.dnb.ASCIIART
import org.json.JSONObject

class WeaponItem(
    val id: Int,
    val damage: Int,
    override val name: String,
    override val description: String,
    override val icon:String = ASCIIART.SWORD
) : EquipmentItem() {

    override val slot = EquipmentSlot.Weapon

    override fun onEquip(owner: Creature) {
        owner.status.weaponAttack += damage
    }

    override fun onUnequip(owner: Creature) {
        owner.status.weaponAttack -= damage
    }

    override fun clone() = WeaponItem(id, damage, name, description, icon)

    override fun toJSON(): JSONObject = super.toJSON().put("t", "w").put("id", id)
}
