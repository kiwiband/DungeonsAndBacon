package kiwiband.dnb.ui.views

import kiwiband.dnb.inventory.Inventory
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.layout.*
import kiwiband.dnb.ui.views.layout.util.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min

class InventoryView(
    private val inventory: Inventory,
    width: Int,
    height: Int
) : View(width, height) {

    val itemHolder =
        object : InteractiveVerticalLayout<InventoryChildView>(ROW_WIDTH, height - 5) {
            override fun createChild(view: View, slot: VerticalSlot): InventoryChildView {
                return InventoryChildView(view, slot, children.size)
            }
        }

    private val header: ChildView<out Slot>
    private val pages: ChildView<out Slot>

    private val rootView = VerticalLayout(width, height).also { vLayout ->
        header = vLayout.addChild(
            Spacer(), VerticalSlot(padding = Padding(bottom = 1), alignment = HorizontalAlignment.CENTER)
        )
        vLayout.addChild(itemHolder, VerticalSlot(verticalSize = Size.FILL, alignment = HorizontalAlignment.CENTER))
        vLayout.addChild(HorizontalLayout(width, 1).also { hLayout ->
            pages = hLayout.addChild(Spacer(), HorizontalSlot(Padding(left = 1)))
            hLayout.addChild(Spacer(), HorizontalSlot(horizontalSize = Size.FILL))
            hLayout.addChild(TextView(BOTTOM_TEXT))
        })
    }

    init {
        for (item in inventory.items) {
            itemHolder.addChild(ItemView(ROW_WIDTH - 2, ROW_HEIGHT - 2, item))
        }
    }

    override fun draw(renderer: Renderer) {
        val space = "${inventory.size}/${inventory.capacity}"
        header.view = TextView("----INVENTORY $space----")

        val itemsOnPage = floor(itemHolder.height.toDouble() / ROW_HEIGHT).toInt()
        val maxPages = ceil(inventory.size.toDouble() / itemsOnPage).toInt()
        val currentPage = if (maxPages == 0 || itemsOnPage == 0) 0 else itemHolder.selected / itemsOnPage + 1

        pages.view = TextView("PAGE $currentPage/${if (maxPages > inventory.size) "???" else maxPages.toString()}")

        val startIndex = (currentPage - 1) * itemsOnPage
        val endIndex = min(inventory.size, startIndex + itemsOnPage)

        itemHolder.limits = startIndex until endIndex

        rootView.resize(rootView.width, rootView.height)
        rootView.draw(renderer)
    }

    override fun resize(width: Int, height: Int) {
        setSize(width, height)
        rootView.resize(width, height)
    }

    companion object {
        private const val ROW_HEIGHT = 7
        private const val ROW_WIDTH = 80
        private const val BOTTOM_TEXT = "(W/S) NAVIGATE | (E) EQUIP/UNEQUIP | (I) EXIT INVENTORY "
    }

    inner class InventoryChildView(
        view: View,
        slot: VerticalSlot,
        val index: Int
    ) : InteractiveChildView<VerticalSlot>(view, slot) {
        private val unselectedView = WrapperLayout(view, WrapperSlot(padding = Padding(1)))
        private val selectedView = BoxLayout(view)

        init {
            setInactive()
        }

        override fun setActive() {
            this.view = selectedView
        }

        override fun setInactive() {
            this.view = unselectedView
        }
    }
}