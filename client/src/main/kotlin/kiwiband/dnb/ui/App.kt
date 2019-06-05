package kiwiband.dnb.ui

import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import kiwiband.dnb.Game
import kiwiband.dnb.InputManager
import kiwiband.dnb.ui.activities.Activity
import kiwiband.dnb.ui.activities.GameActivity
import kiwiband.dnb.ui.activities.LoadMapActivity
import kiwiband.dnb.ui.views.GameOverView
import java.util.*

/**
 * Application class.
 * Run start() method to start
 */
class App {
    private val terminal = DefaultTerminalFactory().createTerminal()
    private val inputManager = InputManager(terminal)
    private val screen = TerminalScreen(terminal)
    private val renderer = Renderer(screen)
    private val activities = ArrayDeque<Activity<*>>()
    private val context = AppContext(renderer, activities)


    /**
     * Console application entry point.
     */
    fun start(playerId: Int = 0) {
        screen.startScreen()
        screen.cursorPosition = null

        inputManager.startKeyHandle()

        val loadMapActivity = LoadMapActivity(context) { map ->
            val game = Game(map, playerId)
            val gameActivity = GameActivity(game, context) { gameResult ->
                inputManager.stop()
                if (gameResult) {
                    screen.clear()
                    GameOverView(SCREEN_WIDTH, SCREEN_HEIGHT).draw(renderer)
                    screen.refresh()
                    Thread.sleep(2000)
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
