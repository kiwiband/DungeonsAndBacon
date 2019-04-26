package kiwiband.dnb.ui

import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import kiwiband.dnb.Game
import kiwiband.dnb.InputManager
import kiwiband.dnb.ui.activities.EventGameActivityFinished
import kiwiband.dnb.ui.activities.EventMapLoaded
import kiwiband.dnb.ui.activities.GameActivity
import kiwiband.dnb.ui.activities.LoadMapActivity

/**
 * Application class.
 * Run start() method to start
 */
class App {
    private val terminal = DefaultTerminalFactory().createTerminal()
    private val inputManager = InputManager(terminal)
    private val screen = TerminalScreen(terminal)
    private val renderer = Renderer(screen)

    /**
     * Runs the first activity.
     */
    private fun startGame() {
        val loadMapActivity = LoadMapActivity(renderer)
        loadMapActivity.start()
    }

    /**
     * Console application entry point.
     */
    fun start() {
        screen.startScreen()
        screen.cursorPosition = null

        inputManager.startKeyHandle()

        // once the map is loaded, we can start the game activity.
        EventMapLoaded.dispatcher.addHandler { event ->
            val game = Game(event.result)
            val gameActivity = GameActivity(game, renderer)
            gameActivity.start()
        }

        // once the game has ended, we can stop the input manager and end the game.
        EventGameActivityFinished.dispatcher.addHandler {
            inputManager.stop()
        }

        startGame()

        // wait for the end of the game here.
        inputManager.join()
        screen.stopScreen()
    }

    companion object {
        const val SCREEN_WIDTH = 80
        const val SCREEN_HEIGHT = 24
    }
}
