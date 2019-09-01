package kiwiband.dnb.ui.activities

import com.googlecode.lanterna.input.KeyType
import kiwiband.dnb.events.EventPressKey
import kiwiband.dnb.inventory.ItemFactory
import kiwiband.dnb.ui.GameAppContext
import kiwiband.dnb.ui.views.*
import kiwiband.dnb.ui.views.layout.*
import kiwiband.dnb.ui.views.layout.util.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min

class InventoryActivity(context: GameAppContext, internal val playerId: String) : Activity<Unit>(context, {}) {
    private val mgr = context.gameManager

    private lateinit var itemHolder: InteractiveSequenceLayout<out SequenceSlot, InventoryChildView>
    private lateinit var header: ChildView<out Slot>
    private lateinit var pages: ChildView<out Slot>
    private lateinit var rootView: VerticalLayout

    override fun createRootView(): View {
        val size = context.renderer.screen.terminalSize
        val width = size.columns
        val height = size.rows

        itemHolder = object : InteractiveVerticalLayout<InventoryChildView>(ROW_WIDTH, height - 5) {
            override fun createChild(view: View, slot: VerticalSlot): InventoryChildView {
                return InventoryChildView(view, slot, children.size)
            }
        }

        rootView = VerticalLayout(width, height).also { vLayout ->
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

        for (item in getInventory().items) {
            itemHolder.addChild(ItemView(ROW_WIDTH - 2, ROW_HEIGHT - 2, item))
        }
        return rootView
    }

    override fun beforeDraw() {
        val inventory = getInventory()
        val space = "${inventory.size}/${inventory.capacity}"
        header.view = TextView("----INVENTORY $space----")

        val itemsOnPage = floor(itemHolder.height.toDouble() / ROW_HEIGHT).toInt()
        val maxPages = ceil(inventory.size.toDouble() / itemsOnPage).toInt()
        val currentPage = if (maxPages == 0 || itemsOnPage == 0) 0 else itemHolder.selected / itemsOnPage + 1

        pages.view = TextView("PAGE $currentPage/${if (maxPages > inventory.size) "???" else maxPages.toString()}")

        val startIndex = (currentPage - 1) * itemsOnPage
        val endIndex = min(inventory.size, startIndex + itemsOnPage)

        itemHolder.limits = startIndex until endIndex

        rootView.updateSize()
    }

    private fun getInventory() = mgr.getPlayer().inventory

    private fun onKeyPressed(pressKey: EventPressKey) {
        if (pressKey.key.keyType == KeyType.Escape) {
            finish(Unit)
            return
        }
        when (pressKey.key.character) {
            'i', 'ш' -> {
                finish(Unit)
            }
            'w', 'ц' -> {
                itemHolder.previous()
                drawScene()
            }
            's', 'ы' -> {
                itemHolder.next()
                drawScene()
            }
            'e', 'у' -> {
                itemHolder.interact()
                drawScene()
            }
            'x', 'ч' -> { // TODO cheat
                getInventory().add(ItemFactory.getRandomItem())
                drawScene()
            }
        }
    }

    override fun onStart() {
        context.eventBus.pressKey.addHandler { onKeyPressed(it) }
    }

    companion object {
        private const val ROW_HEIGHT = 7
        private const val ROW_WIDTH = 80
        private const val BOTTOM_TEXT = "(W/S) NAVIGATE | (E) EQUIP/UNEQUIP | (I) EXIT INVENTORY "
    }

    inner class InventoryChildView(
        view: View,
        slot: VerticalSlot,
        private val index: Int
    ) : BoxedChildView<VerticalSlot>(view, slot) {
        override fun onInteract() {
            mgr.useItem(index, mgr.getPlayer().playerId)
        }
    }
}