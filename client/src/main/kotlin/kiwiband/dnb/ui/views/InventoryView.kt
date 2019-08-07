package kiwiband.dnb.ui.views

import kiwiband.dnb.inventory.Inventory
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.layout.VerticalLayout
import kotlin.math.min

class InventoryView(private val inventory: Inventory, width: Int, height: Int) : View(width, height) {
    private val itemHolder = VerticalLayout(width, height - 5)

    private var currentSelected = 0

    fun selectNext() {
        if (!inventory.isEmpty())
            currentSelected = Math.floorMod(currentSelected + 1, inventory.getSize())
    }

    fun selectPrevious() {
        if (!inventory.isEmpty())
            currentSelected = Math.floorMod(currentSelected - 1, inventory.getSize())
    }

    fun getCurrentSelected(): Int = if (inventory.isEmpty()) -1 else currentSelected

    override fun draw(renderer: Renderer) {
        val space = "${inventory.getSize()}/${inventory.capacity}"
        renderer.writeText("----INVENTORY $space----", Vec2((width - 18 - space.length) / 2, 0))

        val maxPages = (inventory.getSize() + ITEMS_ON_PAGE - 1) / ITEMS_ON_PAGE
        val currentPage = if (maxPages == 0) 0 else currentSelected / ITEMS_ON_PAGE + 1

        renderer.writeText(" PAGE $currentPage/$maxPages", Vec2(0, height - 1))
        renderer.writeText(BOTTOM_TEXT, Vec2(width - BOTTOM_TEXT.length, height - 1))

        if (inventory.isEmpty())
            return

        val startIndex = (currentPage - 1) * ITEMS_ON_PAGE
        val endIndex = min(inventory.getSize(), startIndex + ITEMS_ON_PAGE)

        itemHolder.clear()
        val items = inventory.items()
        for (i in startIndex until endIndex) {
            itemHolder.addChild(ItemView(width, ROW_HEIGHT, items[i]))
        }

        renderer.withOffset {
            renderer.offset.add(Vec2(0, 2))
            itemHolder.draw(renderer)
        }

        val selectedRow = currentSelected % ITEMS_ON_PAGE
        renderer.withOffset {
            renderer.offset.add(Vec2(0, 2 + selectedRow * ROW_HEIGHT))
            renderer.drawBox(width - 2, ROW_HEIGHT)
        }
    }

    override fun resize(width: Int, height: Int) {
        setSize(width, height)
    }

    companion object {
        private const val ITEMS_ON_PAGE = 3
        private const val ROW_HEIGHT = 7
        private const val BOTTOM_TEXT = "(W/S) NAVIGATE | (E) EQUIP/UNEQUIP | (I) EXIT INVENTORY "
    }
}