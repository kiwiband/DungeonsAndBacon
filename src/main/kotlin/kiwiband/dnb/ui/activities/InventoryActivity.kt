package kiwiband.dnb.ui.activities

import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.events.EventActivityFinished
import kiwiband.dnb.events.EventDispatcher
import kiwiband.dnb.events.EventKeyPress
import kiwiband.dnb.ui.App.Companion.SCREEN_HEIGHT
import kiwiband.dnb.ui.App.Companion.SCREEN_WIDTH
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.InventoryView
import kiwiband.dnb.ui.views.View

class InventoryActivity(private val player: Player, renderer: Renderer) : Activity(renderer) {
    private lateinit var inventoryRootView: InventoryView

    override fun createRootView(): View {
        inventoryRootView = InventoryView(player.inventory, SCREEN_WIDTH, SCREEN_HEIGHT)
        return inventoryRootView
    }

    private fun onKeyPressed(keyPress: EventKeyPress) {
        when (keyPress.key.character) {
            'i', 'ш' -> {
                finish()
                EventInventoryClosed.dispatcher.run(EventInventoryClosed())
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
                    player.useItem(itemNum)
                    drawScene()
                }
            }
        }
    }

    override fun onStart() {
        EventKeyPress.dispatcher.addHandler { onKeyPressed(it) }
        drawScene()
    }
}

class EventInventoryClosed: EventActivityFinished<Unit>(Unit) {
    companion object {
        val dispatcher = EventDispatcher<EventInventoryClosed>()
    }
}