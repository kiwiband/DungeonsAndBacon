package kiwiband.dnb

import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import kiwiband.dnb.events.EventBus
import kiwiband.dnb.manager.GameManager
import kiwiband.dnb.manager.LocalGameManager
import kiwiband.dnb.manager.MultiplayerGameManager
import kiwiband.dnb.map.MapSaver
import kiwiband.dnb.ui.AppContext
import kiwiband.dnb.ui.GameAppContext
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.activities.*
import java.util.*
import java.util.concurrent.locks.ReentrantLock

/**
 * Application class.
 * Run start() method to start
 */
class App {
    private val eventBus = EventBus()
    private var terminal = DefaultTerminalFactory().createTerminal()
    private val eventLock = ReentrantLock()
    private val inputManager = InputManager(terminal, eventLock, eventBus)
    private var screen = TerminalScreen(terminal)
    private val renderer = Renderer(screen)
    private val activities = ArrayDeque<Activity<*>>()
    private val context = AppContext(renderer, activities, eventBus)

    /**
     * Console application entry point.
     */
    fun start() {
        screen.startScreen()
        screen.cursorPosition = null

        inputManager.startKeyHandle()

        val loadMapActivity = loadMapActivity { mgr: GameManager, gameOver: () -> Unit ->
            val gameContext = GameAppContext(context, mgr, eventBus)
            val gameActivity = GameActivity(gameContext) { gameResult ->
                inputManager.stop()
                gameOver.invoke()
                if (gameResult) {
                    GameOverActivity(gameContext).start()
                }
            }
            gameActivity.start()
        }
        loadMapActivity.start()

        // once the map is loaded, we can start the game activity.
        terminal.addResizeListener { _, newSize ->
            screen.doResizeIfNecessary()
            activities.forEach {
                it.resize(newSize.rows, newSize.columns)
            }
            activities.lastOrNull()?.drawScene()
        }

        // wait for the end of the game here.
        inputManager.join()

        screen.stopScreen()
    }

    private fun loadMapActivity(callback: (GameManager,  () -> Unit) -> Unit): Activity<out Any> {
        return if (ClientSettings.multiplayer) {
            val commManager = ServerCommunicationManager(Settings.host, Settings.port, eventLock, eventBus)
            MultiplayerLoadMapActivity(context, commManager) { (playerId, map) ->
                callback(MultiplayerGameManager(commManager, playerId, map, eventBus)) { commManager.disconnect() }
            }
        } else {
            val mapSaver = MapSaver()
            LocalLoadMapActivity(context, mapSaver, ClientSettings.mapFile) {
                    map -> callback(LocalGameManager(Game(map, eventBus), mapSaver, ClientSettings.mapFile)) {}
            }
        }
    }
}
