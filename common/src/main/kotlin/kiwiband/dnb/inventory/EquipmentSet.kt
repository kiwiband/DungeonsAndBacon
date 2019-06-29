package kiwiband.dnb.inventory

import kiwiband.dnb.actors.creatures.Creature

class EquipmentSet(val owner: Creature) {
    var weapon: EquipmentItem? = null
    var armor: EquipmentItem? = null

    fun equip(item: EquipmentItem) {
        when (item.slot) {
            EquipmentSlot.Weapon -> weapon = equipInSlot(weapon, item)
            EquipmentSlot.Armor -> armor = equipInSlot(armor, item)
        }
    }

    private fun equipInSlot(slotItem: EquipmentItem?, item: EquipmentItem): EquipmentItem? {
        slotItem?.unequip()
        return if (slotItem === item) null else item.also { it.equipBy(owner) }
    }
}