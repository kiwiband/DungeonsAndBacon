package kiwiband.dnb.inventory

import kiwiband.dnb.actors.creatures.Creature

class EquipmentSet(val owner: Creature) {
    var weapon: EquipmentItem? = null
    var armor: EquipmentItem? = null

    fun equip(item: EquipmentItem) {
        when (item.getSlot()) {
            EquipmentSlot.Weapon -> weapon = equipInSlot(weapon, item)
            EquipmentSlot.Armor -> armor = equipInSlot(armor, item)
        }
    }

    private fun equipInSlot(slotItem: EquipmentItem?, item: EquipmentItem): EquipmentItem? {
        slotItem?.onUnequip(owner)
        return if (slotItem === item) null else item.apply { onEquip(owner) }
    }
}