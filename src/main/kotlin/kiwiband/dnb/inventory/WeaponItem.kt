package kiwiband.dnb.inventory

import kiwiband.dnb.actors.creatures.Creature

class WeaponItem(
    private val damage: Int,
    private val name: String,
    private val description: String
) : EquipmentItem() {
    override fun getName() = name

    override fun getDescription() = description

    override fun getSlot() = EquipmentSlot.Weapon

    override fun onEquip(creature: Creature) {
        creature.status.weaponAttack += damage;
    }

    override fun onUnequip(creature: Creature) {
        creature.status.weaponAttack -= damage
    }

    override fun clone() = WeaponItem(damage, name, description)
}
