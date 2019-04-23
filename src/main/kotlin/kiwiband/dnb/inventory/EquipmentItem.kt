package kiwiband.dnb.inventory

import kiwiband.dnb.actors.creatures.Creature

abstract class EquipmentItem : Item() {
    abstract fun getSlot(): EquipmentSlot

    open fun onEquip(creature: Creature) {}

    open fun onUnequip(creature: Creature) {}
}