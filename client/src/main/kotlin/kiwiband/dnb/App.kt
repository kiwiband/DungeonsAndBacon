package kiwiband.dnb

import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import kiwiband.dnb.events.EventBus
import kiwiband.dnb.manager.MultiplayerGameManager
import kiwiband.dnb.ui.AppContext
import kiwiband.dnb.ui.GameAppContext
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.activities.Activity
import kiwiband.dnb.ui.activities.GameActivity
import kiwiband.dnb.ui.activities.GameOverActivity
import kiwiband.dnb.ui.activities.LoadMapActivity
import java.util.*
import java.util.concurrent.locks.ReentrantLock

/**
 * Application class.
 * Run start() method to start
 */
class App() {
    private val eventBus = EventBus()
    private val terminal = DefaultTerminalFactory().createTerminal()
    private val eventLock = ReentrantLock()
    private val inputManager = InputManager(terminal, eventLock, eventBus)
    private val screen = TerminalScreen(terminal)
    private val renderer = Renderer(screen)
    private val activities = ArrayDeque<Activity<*>>()
    private val context = AppContext(renderer, activities, eventBus)
    private val serverCommunicationManager = ServerCommunicationManager(Settings.host, Settings.port, eventLock, eventBus)


    /**
     * Console application entry point.
     */
    fun start() {
        screen.startScreen()
        screen.cursorPosition = null

        inputManager.startKeyHandle()

        val loadMapActivity = LoadMapActivity(context, serverCommunicationManager) { (playerId, map) ->
            val mgr = MultiplayerGameManager(serverCommunicationManager, playerId, map, eventBus)
            val gameContext = GameAppContext(context, mgr, eventBus)
            val gameActivity = GameActivity(gameContext) { gameResult ->
                inputManager.stop()
                serverCommunicationManager.disconnect()
                if (gameResult) {
                    GameOverActivity(gameContext).start()
                }
            }
            gameActivity.start()
        }
        loadMapActivity.start()

        // once the map is loaded, we can start the game activity.


        // wait for the end of the game here.
        inputManager.join()

        screen.stopScreen()
    }

    companion object {
        const val SCREEN_WIDTH = 80
        const val SCREEN_HEIGHT = 24
    }
}
