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
import kiwiband.dnb.ui.views.*
import kiwiband.dnb.ui.views.layout.BoxLayout
import kiwiband.dnb.ui.views.layout.HorizontalLayout
import kiwiband.dnb.ui.views.layout.VerticalLayout

/**
 * Application class.
 * Run start() method to start
 */
class App {
    private val gameRootView = HorizontalLayout(SCREEN_WIDTH, SCREEN_HEIGHT)
    private var rootView: View = gameRootView

    private val terminal = DefaultTerminalFactory().createTerminal()

    private val inputManager = InputManager(terminal)
    private val screen = TerminalScreen(terminal)
    private lateinit var game: Game

    private val mapSaver = MapSaver()
    private val mapFile = "./maps/saved_map.dnb"

    private val renderer = Renderer(screen)

    private fun drawScene() {
        screen.clear()

        renderer.setOffset(0, 0)
        rootView.draw(renderer)

        screen.refresh()
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
            EventMove.dispatcher.run(EventMove(it))
            tick()
        }
    }

    private fun tick() {
        EventTick.dispatcher.run(EventTick())
        drawScene()
    }

    private fun constructScene() {
        val mapView = MapView(game.map, 48, 22)
        val playerView = PlayerView(game.player, 28, 10)
        val infoView = InfoView(28, 10)

        gameRootView.addChild(BoxLayout(mapView))

        val sidebar = VerticalLayout(30, 24)
        sidebar.addChild(BoxLayout(infoView))
        sidebar.addChild(BoxLayout(playerView))

        gameRootView.addChild(sidebar)
    }

    private fun createGame() {
        if (!mapSaver.checkSaved(mapFile)) {
            game = Game(LocalMap.generateMap(88, 32))
            return
        }

        gameRootView.addChild(BoxLayout(LoadMapView(SCREEN_WIDTH - 2, SCREEN_HEIGHT - 2)))

        drawScene()

        var map: LocalMap? = null
        val mapLock = Object()
        val eventKeyPressId = EventKeyPress.dispatcher.addHandler {
            synchronized(mapLock) {
                map = when (it.key.character) {
                    'y', 'н' -> mapSaver.loadFromFile(mapFile)
                    'n', 'т' -> LocalMap.generateMap(88, 32)
                    else -> return@addHandler
                }
                mapLock.notify()
            }
        }

        synchronized(mapLock) {
            while (map == null) {
                mapLock.wait()
            }
        }

        game = Game(map!!)

        EventKeyPress.dispatcher.removeHandler(eventKeyPressId)
        gameRootView.clear()
    }

    private fun saveMap() {
        mapSaver.saveToFile(game.map, mapFile)
    }

    private fun deleteMap() {
        mapSaver.deleteFile(mapFile)
    }

    private fun openInventory() {
        EventKeyPress.dispatcher.pushLayer()
        val inventoryRootView = InventoryView(SCREEN_WIDTH, SCREEN_HEIGHT)
        val previousRootView = rootView
        rootView = inventoryRootView
        drawScene()
        EventKeyPress.dispatcher.addHandler {
            if (it.key.character == 'i') {
                EventKeyPress.dispatcher.popLayer()
                rootView = previousRootView
                drawScene()
            }
        }
    }

    /**
     * Console application entry point.
     */
    fun start() {
        screen.startScreen()
        screen.cursorPosition = null

        inputManager.startKeyHandle()

        createGame()
        constructScene()

        drawScene()

        val eventKeyPressId = EventKeyPress.dispatcher.addHandler { handleMoveKeys(it.key) }
        val eventEscapeId = EventKeyPress.dispatcher.addHandler {
            if (it.key.keyType == KeyType.Escape) {
                inputManager.stop()
            }
        }
        val eventGameOverId = EventGameOver.dispatcher.addHandler {
            inputManager.stop()
        }

        val eventInventoryOpenId = EventKeyPress.dispatcher.addHandler {
            if (it.key.keyType == KeyType.Character) {
                when (it.key.character) {
                    'i', 'ш' -> openInventory()
                }
            }
        }

        game.startGame()
        inputManager.join()
        game.endGame()

        EventKeyPress.dispatcher.removeHandler(eventKeyPressId)
        EventKeyPress.dispatcher.removeHandler(eventEscapeId)
        EventGameOver.dispatcher.removeHandler(eventGameOverId)
        EventGameOver.dispatcher.removeHandler(eventInventoryOpenId)

        if (game.player.isDead()) {
            deleteMap()
            gameRootView.clear()
            gameRootView.addChild(BoxLayout(GameOverView(SCREEN_WIDTH - 2, SCREEN_HEIGHT - 2)))
            drawScene()
            Thread.sleep(2000)
        } else {
            saveMap()
        }

        screen.stopScreen()
    }

    companion object {
        private const val SCREEN_WIDTH = 80
        private const val SCREEN_HEIGHT = 24
    }
}
