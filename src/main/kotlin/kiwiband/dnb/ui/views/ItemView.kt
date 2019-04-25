package kiwiband.dnb.ui.views

import kiwiband.dnb.inventory.*
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer

class ItemView(width: Int, height: Int, private val item: EquipmentItem) : View(width, height) {
    override fun draw(renderer: Renderer) {
        renderer.withOffset {
            renderer.offset.add(Vec2(5, 2))
            drawItemIcon(renderer)
        }
        renderer.writeText(item.getName(), Vec2(13, 2))
        renderer.writeText(itemStatString(), Vec2(13, 3))
        renderer.writeText(item.getDescription(), Vec2(13, 4))
    }

    private fun itemStatString() = when (item) {
        is ArmorItem -> "${item.defence} DEF"
        is WeaponItem -> "${item.damage} ATK"
        else -> ""
    }

    private fun drawItemIcon(renderer: Renderer) {
        when (item.getSlot()) {
            EquipmentSlot.Armor -> {
                renderer.writeText(".-.-.", Vec2(0, 0))
                renderer.writeText("| . |", Vec2(0, 1))
                renderer.writeText("\\___/", Vec2(0, 2))
            }
            EquipmentSlot.Weapon -> {
                renderer.writeText("  | ", Vec2(0, 0))
                renderer.writeText("  ║ ", Vec2(0, 1))
                renderer.writeText(" `T`", Vec2(0, 2))
            }
        }
    }
}