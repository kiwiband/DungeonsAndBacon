package kiwiband.dnb.ui.views

import kiwiband.dnb.inventory.EquipmentItem
import kiwiband.dnb.inventory.ItemFactory
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.layout.VerticalLayout
import kotlin.math.min

class InventoryView(width: Int, height: Int) : View(width, height) {
    private val items = mutableListOf<EquipmentItem>(
        ItemFactory.getRandomItem() as EquipmentItem,
        ItemFactory.getRandomItem() as EquipmentItem,
        ItemFactory.getRandomItem() as EquipmentItem,
        ItemFactory.getRandomItem() as EquipmentItem,
        ItemFactory.getRandomItem() as EquipmentItem
    )

    private val itemHolder = VerticalLayout(width, height - 5)

    private var currentSelected = 0

    fun selectNext() {
        if (items.isEmpty())
            return
        currentSelected = (currentSelected + 1) % items.size
    }

    fun selectPrevious() {
        if (items.isEmpty())
            return
        currentSelected = (currentSelected + items.size - 1) % items.size
    }

    @SuppressWarnings("unchecked")
    fun getCurrentSelected(): Int {
        return currentSelected
    }

    override fun draw(renderer: Renderer) {
        renderer.writeText("----INVENTORY----", Vec2(28, 0))

        val maxPages = (items.size + ITEMS_ON_PAGE - 1) / ITEMS_ON_PAGE
        val currentPage = if (maxPages == 0) 0 else currentSelected / ITEMS_ON_PAGE + 1

        renderer.writeText("PAGE $currentPage/$maxPages", Vec2(0, height - 1))
        renderer.writeText(BOTTOM_TEXT, Vec2(width - BOTTOM_TEXT.length, height - 1))

        if (items.isEmpty())
            return

        val startIndex = (currentPage - 1) * ITEMS_ON_PAGE
        val endIndex = min(items.size, startIndex + ITEMS_ON_PAGE)

        itemHolder.clear()
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

    companion object {
        private const val ITEMS_ON_PAGE = 3
        private const val ROW_HEIGHT = 7
        private const val BOTTOM_TEXT = "(W/S) NAVIGATE | (E) EQUIP/UNEQUIP | (I) EXIT INVENTORY"
    }
}