package kiwiband.dnb.inventory

import kiwiband.dnb.actors.creatures.Creature
import org.json.JSONObject

abstract class EquipmentItem : Item() {
    var owner: Creature? = null

    abstract val slot: EquipmentSlot

    fun equipBy(owner: Creature) {
        unequip()
        this.owner = owner
        onEquip(owner)
    }

    fun unequip() {
        if (equipped()) {
            onUnequip(owner!!)
            owner = null
        }
    }

    fun equipped() = owner != null

    protected abstract fun onEquip(owner: Creature)

    protected abstract fun onUnequip(owner: Creature)

    override fun toJSON(): JSONObject = JSONObject().put("eqp", equipped())
}