package kiwiband.dnb.ui.views

import kiwiband.dnb.inventory.*
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer

class ItemView(width: Int, height: Int, private val item: Item) : View(width, height) {
    override fun draw(renderer: Renderer) {
        if (item is EquipmentItem && item.equipped()) {
            renderer.writeCharacter('*', Vec2(2, 2))
        }
        renderer.withOffset {
            renderer.offset.add(Vec2(4, 1))
            renderer.writeMultiLineText(item.icon)
        }
        renderer.writeText(item.name, Vec2(12, 1))
        renderer.writeText(itemStatString(), Vec2(12, 2))
        renderer.writeText(item.description, Vec2(12, 3))
    }

    private fun itemStatString() = when (item) {
        is ArmorItem -> "${item.defence} DEF"
        is WeaponItem -> "${item.damage} ATK"
        else -> ""
    }

}