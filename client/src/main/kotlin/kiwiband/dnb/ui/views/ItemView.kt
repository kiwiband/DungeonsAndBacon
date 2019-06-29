package kiwiband.dnb.ui.views

import kiwiband.dnb.inventory.*
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer

class ItemView(width: Int, height: Int, private val item: Item) : View(width, height) {
    override fun draw(renderer: Renderer) {
        if (item is EquipmentItem && item.equipped()) {
            renderer.writeCharacter('*', Vec2(3, 3))
        }
        renderer.withOffset {
            renderer.offset.add(Vec2(5, 2))
            renderer.writeMultiLineText(item.icon)
        }
        renderer.writeText(item.name, Vec2(13, 2))
        renderer.writeText(itemStatString(), Vec2(13, 3))
        renderer.writeText(item.description, Vec2(13, 4))
    }

    private fun itemStatString() = when (item) {
        is ArmorItem -> "${item.defence} DEF"
        is WeaponItem -> "${item.damage} ATK"
        else -> ""
    }

}