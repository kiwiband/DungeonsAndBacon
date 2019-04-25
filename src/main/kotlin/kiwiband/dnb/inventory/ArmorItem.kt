package kiwiband.dnb.inventory

import kiwiband.dnb.actors.creatures.Creature
import kiwiband.dnb.ui.ASCIIART

class ArmorItem(
    val defence: Int,
    private val name: String,
    private val description: String,
    private val icon:String = ASCIIART.SHIELD
) : EquipmentItem() {
    override fun getName() = name

    override fun getIcon() = icon

    override fun getSlot() = EquipmentSlot.Armor

    override fun getDescription() = description

    override fun onEquip(creature: Creature) {
        super.onEquip(creature)
        creature.status.armorDefence += defence
    }

    override fun onUnequip(creature: Creature) {
        super.onUnequip(creature)
        creature.status.armorDefence -= defence
    }

    override fun clone() = ArmorItem(defence, name, description)
}