package kiwiband.dnb.ui.activities

import kiwiband.dnb.events.EventPressKey
import kiwiband.dnb.ui.GameAppContext
import kiwiband.dnb.ui.views.InventoryView
import kiwiband.dnb.ui.views.View

class InventoryActivity(context: GameAppContext, private val playerId: Int) : Activity<Unit>(context, {}) {
    private lateinit var inventoryRootView: InventoryView

    private val mgr = context.gameManager

    override fun createRootView(): View {
        val size = context.renderer.screen.terminalSize
        inventoryRootView = InventoryView(mgr.getInventory(), size.columns, size.rows)
        return inventoryRootView
    }

    private fun onKeyPressed(pressKey: EventPressKey) {
        when (pressKey.key.character) {
            'i', 'ш' -> {
                finish(Unit)
            }
            'w', 'ц' -> {
                inventoryRootView.selectPrevious()
                drawScene()
            }
            's', 'ы' -> {
                inventoryRootView.selectNext()
                drawScene()
            }
            'e', 'у' -> {
                val itemNum = inventoryRootView.getCurrentSelected()
                if (itemNum >= 0) {
                    mgr.useItem(itemNum, playerId)//player.useItem(itemNum)
                    drawScene()
                }
            }
        }
    }

    override fun onStart() {
        context.eventBus.pressKey.addHandler { onKeyPressed(it) }
        drawScene()
    }
}