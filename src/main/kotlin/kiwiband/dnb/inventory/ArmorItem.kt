package kiwiband.dnb.inventory

import kiwiband.dnb.actors.creatures.Creature

class ArmorItem(
    private val defence: Int,
    private val name: String,
    private val description: String
) : EquipmentItem() {
    override fun getName() = name

    override fun getSlot() = EquipmentSlot.Armor

    override fun getDescription() = description

    override fun onEquip(creature: Creature) {
        creature.status.armorDefence += defence
    }

    override fun onUnequip(creature: Creature) {
        creature.status.armorDefence -= defence
    }

    override fun clone() = ArmorItem(defence, name, description)
}