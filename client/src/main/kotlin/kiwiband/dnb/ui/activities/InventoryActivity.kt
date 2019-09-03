package kiwiband.dnb.ui.activities

import com.googlecode.lanterna.input.KeyType
import kiwiband.dnb.events.EventPressKey
import kiwiband.dnb.inventory.ItemFactory
import kiwiband.dnb.ui.GameAppContext
import kiwiband.dnb.ui.views.*
import kiwiband.dnb.ui.views.layout.*
import kiwiband.dnb.ui.views.layout.util.*
import kiwiband.dnb.ui.views.layout.util.HorizontalAlignment.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min

class InventoryActivity(context: GameAppContext, internal val playerId: String) : Activity<Unit>(context, {}) {
    private val mgr = context.gameManager

    private lateinit var itemHolder: InteractiveSequenceLayout<out SequenceNode, InventorySlot>
    private lateinit var header: Slot<out Node>
    private lateinit var pages: Slot<out Node>
    private lateinit var rootView: VerticalLayout

    override fun createRootView(): View {
        val size = context.renderer.screenSize

        itemHolder = object : InteractiveVerticalLayout<InventorySlot>(ROW_WIDTH, size.y - 5) {
            override fun createSlot(view: View, node: VerticalNode): InventorySlot {
                return InventorySlot(view, node, children.size)
            }
        }

        rootView = VerticalLayout(size.x, size.y).also { vLayout ->
            header = vLayout.add(
                Spacer(), VerticalNode(CENTER, Padding(bottom = 1))
            )
            vLayout.add(itemHolder, VerticalNode(CENTER, verticalSize = Size.FILL))
            vLayout.add(HorizontalLayout(size.x, 1).also { hLayout ->
                pages = hLayout.add(Spacer(), HorizontalNode(padding = Padding(left = 1)))
                hLayout.add(Spacer(), HorizontalNode(horizontalSize = Size.FILL))
                hLayout.add(TextView(BOTTOM_TEXT))
            })
        }

        for (item in getInventory().items) {
            itemHolder.add(ItemView(ROW_WIDTH - 2, ROW_HEIGHT - 2, item))
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

    inner class InventorySlot(
        view: View,
        slot: VerticalNode,
        private val index: Int
    ) : BoxedSlot<VerticalNode>(view, slot) {
        override fun onInteract() {
            mgr.useItem(index, mgr.getPlayer().playerId)
        }
    }
}