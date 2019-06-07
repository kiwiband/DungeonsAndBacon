package kiwiband.dnb.ui.activities

import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import kiwiband.dnb.events.EventGameOver
import kiwiband.dnb.events.EventKeyPress
import kiwiband.dnb.events.EventTick
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.App
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

        val mapView = MapView(mgr, 48, 22)
        val playerView = PlayerView(mgr, 28, 10)
        val infoView = InfoView(28, 10)

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
            else -> null
        }?.also {
            mgr.movePlayer(it)
        }
    }

    private fun onTick() {
        if (context.activities.peekLast() == this)
            drawScene()
    }

    private fun openInventory() {
        InventoryActivity(gameContext) {}.start()
    }

    override fun onStart() {
        drawScene()

        EventKeyPress.dispatcher.addHandler {
            if (it.key.keyType == KeyType.Escape) {
                EventGameOver.dispatcher.run(EventGameOver())
            }
        }

        EventTick.dispatcher.addHandler { onTick() }

        EventKeyPress.dispatcher.addHandler { handleMoveKeys(it.key) }

        EventKeyPress.dispatcher.addHandler {
            if (it.key.keyType == KeyType.Character) {
                when (it.key.character) {
                    'i', 'ш' -> openInventory()
                }
            }
        }

        EventGameOver.dispatcher.addHandler {
            val isDead = mgr.finishGame()
            finish(isDead)
        }

        mgr.startGame()
    }

    override fun onFinish(result: Boolean) {

    }
}