package kiwiband.dnb.ui

import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import kiwiband.dnb.Game
import kiwiband.dnb.InputManager
import kiwiband.dnb.events.EventKeyPress
import kiwiband.dnb.events.EventMove
import kiwiband.dnb.events.EventTick
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.views.InfoView
import kiwiband.dnb.ui.views.MapView
import kiwiband.dnb.ui.views.PlayerView
import kiwiband.dnb.ui.views.layout.BoxLayout
import kiwiband.dnb.ui.views.layout.HorizontalLayout
import kiwiband.dnb.ui.views.layout.VerticalLayout

class App {
    private val rootView = HorizontalLayout(SCREEN_WIDTH, SCREEN_HEIGHT)

    private val terminal = DefaultTerminalFactory().createTerminal()

    private val inputManager = InputManager(terminal)
    private val screen = TerminalScreen(terminal)
    private val game = Game()

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
            'w', 'ц' -> Vec2M(0, -1)
            'a', 'ф' -> Vec2M(-1, 0)
            's', 'ы' -> Vec2M(0, 1)
            'd', 'в' -> Vec2M(1, 0)
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
        val playerView = PlayerView(28, 10)
        val infoView = InfoView(28, 10)

        rootView.addChild(BoxLayout(mapView))

        val sidebar = VerticalLayout(30, 24)
        sidebar.addChild(BoxLayout(infoView))
        sidebar.addChild(BoxLayout(playerView))

        rootView.addChild(sidebar)
    }

    fun start() {
        screen.startScreen()
        screen.cursorPosition = null
        constructScene()

        inputManager.startKeyHandle()
        val eventKeyPressId = EventKeyPress.dispatcher.addHandler { handleMoveKeys(it.key) }

        drawScene()
        game.startGame()
        inputManager.join()
        screen.stopScreen()
        game.endGame()
        EventKeyPress.dispatcher.removeHandler(eventKeyPressId)
    }

    companion object {
        private const val SCREEN_WIDTH = 80
        private const val SCREEN_HEIGHT = 24
    }
}

fun main() {
    App().start()
}
