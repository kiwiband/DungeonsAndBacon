package kiwiband.dnb.ui

import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import kiwiband.dnb.InputManager
import kiwiband.dnb.ServerCommunicationManager
import kiwiband.dnb.manager.MultiplayerGameManager
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
class App(host: String, port: Int) {
    private val terminal = DefaultTerminalFactory().createTerminal()
    private val eventLock = ReentrantLock()
    private val inputManager = InputManager(terminal, eventLock)
    private val screen = TerminalScreen(terminal)
    private val renderer = Renderer(screen)
    private val activities = ArrayDeque<Activity<*>>()
    private val context = AppContext(renderer, activities)
    private val serverCommunicationManager = ServerCommunicationManager(host, port, eventLock)


    /**
     * Console application entry point.
     */
    fun start() {
        screen.startScreen()
        screen.cursorPosition = null

        inputManager.startKeyHandle()

        val loadMapActivity = LoadMapActivity(context, serverCommunicationManager) { (playerId, map) ->
            val mgr = MultiplayerGameManager(serverCommunicationManager, playerId, map)
            val gameContext = GameAppContext(context, mgr)
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
