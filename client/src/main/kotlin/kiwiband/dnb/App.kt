package kiwiband.dnb

import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import kiwiband.dnb.events.EventBus
import kiwiband.dnb.ui.AppContext
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.activities.Activity
import kiwiband.dnb.ui.activities.MainMenuActivity
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

        eventBus.closeGame.addHandler {
            inputManager.stop()
        }


        MainMenuActivity(context, eventLock).start()

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
}
