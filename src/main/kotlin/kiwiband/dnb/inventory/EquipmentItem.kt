package kiwiband.dnb.inventory

import kiwiband.dnb.actors.creatures.Creature

abstract class EquipmentItem : Item() {
    var equipped = false
        private set

    abstract fun getSlot(): EquipmentSlot

    open fun onEquip(creature: Creature) {
        equipped = true
    }

    open fun onUnequip(creature: Creature) {
        equipped = false
    }
}