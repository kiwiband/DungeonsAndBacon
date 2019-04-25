package kiwiband.dnb.ui.views

import kiwiband.dnb.inventory.*
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.ASCIIART
import kiwiband.dnb.ui.Renderer

class ItemView(width: Int, height: Int, private val item: Item) : View(width, height) {
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
        renderer.writeMultiLineText(item.getIcon())
    }
}