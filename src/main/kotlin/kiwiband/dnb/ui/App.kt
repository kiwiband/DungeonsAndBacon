package kiwiband.dnb.ui

import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import kiwiband.dnb.Game
import kiwiband.dnb.InputManager
import kiwiband.dnb.events.EventGameOver
import kiwiband.dnb.events.EventKeyPress
import kiwiband.dnb.events.EventMove
import kiwiband.dnb.events.EventTick
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.map.MapSaver
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.activities.EventGameActivityFinished
import kiwiband.dnb.ui.activities.EventMapLoaded
import kiwiband.dnb.ui.activities.GameActivity
import kiwiband.dnb.ui.activities.LoadMapActivity
import kiwiband.dnb.ui.views.*
import kiwiband.dnb.ui.views.layout.BoxLayout
import kiwiband.dnb.ui.views.layout.HorizontalLayout
import kiwiband.dnb.ui.views.layout.VerticalLayout

/**
 * Application class.
 * Run start() method to start
 */
class App {
    private val terminal = DefaultTerminalFactory().createTerminal()
    private val inputManager = InputManager(terminal)
    private val screen = TerminalScreen(terminal)
    private val renderer = Renderer(screen)

    private fun constructScene(game: Game): View {
        val gameRootView = HorizontalLayout(SCREEN_WIDTH, SCREEN_HEIGHT)

        val mapView = MapView(game.map, 48, 22)
        val playerView = PlayerView(game.player, 28, 10)
        val infoView = InfoView(28, 10)

        gameRootView.addChild(BoxLayout(mapView))

        val sidebar = VerticalLayout(30, 24)
        sidebar.addChild(BoxLayout(infoView))
        sidebar.addChild(BoxLayout(playerView))

        gameRootView.addChild(sidebar)
        return gameRootView
    }

    private fun createGame() {
        val view = BoxLayout(LoadMapView(SCREEN_WIDTH - 2, SCREEN_HEIGHT - 2))
        val loadMapActivity = LoadMapActivity(view, renderer)
        loadMapActivity.start()
    }
/*
    private fun openInventory(game: Game) {
        EventKeyPress.dispatcher.pushLayer()
        val inventoryRootView = InventoryView(game.player.inventory, SCREEN_WIDTH, SCREEN_HEIGHT)
        val previousRootView = rootView
        rootView = inventoryRootView
        drawScene()
        EventKeyPress.dispatcher.addHandler {
            when (it.key.character) {
                'i', 'ш' -> {
                    EventKeyPress.dispatcher.popLayer()
                    rootView = previousRootView
                    drawScene()
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
                        game.player.useItem(itemNum)
                        drawScene()
                    }
                }
            }
        }
    }
*/
    /**
     * Console application entry point.
     */
    fun start() {
        screen.startScreen()
        screen.cursorPosition = null

        inputManager.startKeyHandle()

        EventMapLoaded.dispatcher.addHandler { event ->
            val game = Game(event.result)

            val scene = constructScene(game)

            val gameActivity = GameActivity(game, scene, renderer)

            gameActivity.start()
        }

        EventGameActivityFinished.dispatcher.addHandler {
            inputManager.stop()
        }

        createGame()

        inputManager.join()
        screen.stopScreen()
    }

    companion object {
        private const val SCREEN_WIDTH = 80
        private const val SCREEN_HEIGHT = 24
    }
}
