package kiwiband.dnb.ui.activities

import kiwiband.dnb.events.EventPressKey
import kiwiband.dnb.App.Companion.SCREEN_HEIGHT
import kiwiband.dnb.App.Companion.SCREEN_WIDTH
import kiwiband.dnb.ui.GameAppContext
import kiwiband.dnb.ui.views.InventoryView
import kiwiband.dnb.ui.views.View

class InventoryActivity(context: GameAppContext, private val playerId: Int) : Activity<Unit>(context, {}) {
    private lateinit var inventoryRootView: InventoryView

    private val mgr = context.gameManager

    override fun createRootView(): View {
        inventoryRootView = InventoryView(mgr.getInventory(), SCREEN_WIDTH, SCREEN_HEIGHT)
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