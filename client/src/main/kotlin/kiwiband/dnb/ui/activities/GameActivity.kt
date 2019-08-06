package kiwiband.dnb.ui.activities

import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import kiwiband.dnb.events.EventGameOver
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.App
import kiwiband.dnb.Settings
import kiwiband.dnb.events.TickOrder
import kiwiband.dnb.ui.GameAppContext
import kiwiband.dnb.ui.views.InfoView
import kiwiband.dnb.ui.views.MapView
import kiwiband.dnb.ui.views.PlayerView
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.BoxLayout
import kiwiband.dnb.ui.views.layout.HorizontalLayout
import kiwiband.dnb.ui.views.layout.VerticalLayout

class GameActivity(
    private val gameContext: GameAppContext,
    callback: (Boolean) -> Unit
) : Activity<Boolean>(gameContext, callback) {

    private val mgr = gameContext.gameManager

    override fun createRootView(): View {
        val gameRootView = HorizontalLayout(App.SCREEN_WIDTH, App.SCREEN_HEIGHT)

        val mapView = MapView(gameContext, 48, 22)
        val playerView = PlayerView(mgr, 28, 10)
        val infoView = InfoView(gameContext.selection, 28, 10)

        gameRootView.addChild(BoxLayout(mapView))

        val sidebar = VerticalLayout(30, 24)
        sidebar.addChild(BoxLayout(infoView))
        sidebar.addChild(BoxLayout(playerView))

        gameRootView.addChild(sidebar)
        return gameRootView
    }

    private fun handleMoveKeys(keyStroke: KeyStroke) {
        if (keyStroke.keyType != KeyType.Character) return
        when (keyStroke.character) {
            'w', 'ц' -> Vec2(0, -1)
            'a', 'ф' -> Vec2(-1, 0)
            's', 'ы' -> Vec2(0, 1)
            'd', 'в' -> Vec2(1, 0)
            'e', 'у' -> Vec2(1, -1)
            'q', 'й' -> Vec2(-1, -1)
            'z', 'я' -> Vec2(-1, 1)
            'c', 'с' -> Vec2(1, 1)
            ' ' -> Vec2(0, 0)
            else -> null
        }?.also {
            mgr.movePlayer(it)
        }
    }

    private fun handleActionsKey(key: KeyStroke) {
        when(key.keyType) {
            KeyType.Tab -> {
                gameContext.selection.nextSelection(mgr.getPlayer(), Settings.fovRadius)
                drawScene()
            }
            KeyType.Character -> when (key.character) {
                'i', 'ш' -> openInventory()
            }
            else -> return
        }
    }

    private fun handleEscapeKey(key: KeyStroke) {
        if (key.keyType == KeyType.Escape) {
            context.eventBus.run(EventGameOver())
        }
    }

    private fun onTick() {
        if (context.activities.peekLast() == this)
            updateSelection()
            drawScene()
    }

    private fun updateSelection() {
        gameContext.selection.updateSelection(mgr.getPlayer(), Settings.fovRadius)
    }

    private fun openInventory() {
        InventoryActivity(gameContext, mgr.getPlayer().playerId).start()
    }

    override fun onStart() {
        context.eventBus.pressKey.addHandler {
            handleEscapeKey(it.key)
            handleMoveKeys(it.key)
            handleActionsKey(it.key)
        }

        context.eventBus.tick.addHandler(TickOrder.DRAW_UI) { onTick() }

        context.eventBus.gameOver.addHandler {
            val isDead = mgr.finishGame()
            finish(isDead)
        }

        mgr.startGame()
        updateSelection()
        drawScene()
    }

    override fun onFinish(result: Boolean) {

    }
}