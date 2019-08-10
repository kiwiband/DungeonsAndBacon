package kiwiband.dnb.ui.activities

import com.googlecode.lanterna.input.KeyType
import kiwiband.dnb.events.EventPressKey
import kiwiband.dnb.inventory.ItemFactory
import kiwiband.dnb.ui.GameAppContext
import kiwiband.dnb.ui.views.InventoryAction
import kiwiband.dnb.ui.views.InventoryView
import kiwiband.dnb.ui.views.View

class InventoryActivity(context: GameAppContext, private val playerId: Int) : Activity<Unit>(context, {}) {
    private lateinit var inventoryRootView: InventoryView

    private val mgr = context.gameManager

    override fun createRootView(): View {
        val size = context.renderer.screen.terminalSize
        inventoryRootView = InventoryView(mgr, size.columns, size.rows)
        return inventoryRootView
    }

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
                inventoryRootView.itemHolder.previous()
                drawScene()
            }
            's', 'ы' -> {
                inventoryRootView.itemHolder.next()
                drawScene()
            }
            'e', 'у' -> {
                inventoryRootView.itemHolder.current()?.interact(InventoryAction.USE)
                drawScene()
            }
            'x', 'ч' -> {
                mgr.getPlayer().inventory.add(ItemFactory.getRandomItem())
                drawScene()
            }
        }
    }

    override fun onStart() {
        context.eventBus.pressKey.addHandler { onKeyPressed(it) }
        drawScene()
    }
}