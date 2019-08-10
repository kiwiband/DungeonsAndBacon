package kiwiband.dnb.ui.activities

import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import kiwiband.dnb.Settings
import kiwiband.dnb.events.EventCloseGame
import kiwiband.dnb.events.Registration
import kiwiband.dnb.events.TickOrder
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.GameAppContext
import kiwiband.dnb.ui.views.InfoView
import kiwiband.dnb.ui.views.MapView
import kiwiband.dnb.ui.views.PlayerView
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.*
import kiwiband.dnb.ui.views.layout.util.Size

class GameActivity(
    private val gameContext: GameAppContext,
    callback: (Boolean) -> Unit
) : Activity<Boolean>(gameContext, callback) {
    private val registrations = mutableListOf<Registration>()
    private val mgr = gameContext.gameManager

    override fun createRootView(): View {
        val size = context.renderer.screen.terminalSize
        val gameRootView = HorizontalLayout(size.columns, size.rows)

        val mapView = MapView(gameContext, 48, 22)
        val playerView = PlayerView(mgr, 28, 10)
        val infoView = InfoView(gameContext.selection, 28, 10)

        gameRootView.addChild(BoxLayout(mapView), HorizontalSlot(horizontalSize = Size.FILL))

        val sidebar = VerticalLayout(30, 24)
        sidebar.addChild(BoxLayout(infoView), VerticalSlot())
        sidebar.addChild(BoxLayout(playerView), VerticalSlot(verticalSize = Size.FILL))

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
            context.eventBus.run(EventCloseGame())
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
        registrations.add(context.eventBus.pressKey.addHandler {
            handleEscapeKey(it.key)
            handleMoveKeys(it.key)
            handleActionsKey(it.key)
        })

        registrations.add(context.eventBus.tick.addHandler(TickOrder.DRAW_UI) { onTick() })

        registrations.add(context.eventBus.gameOver.addHandler {
            finish(!mgr.getPlayer().alive)
        })

        mgr.startGame()
        updateSelection()
        drawScene()
    }

    override fun onFinish(result: Boolean) {
        registrations.forEach { it.finish() }
    }
}