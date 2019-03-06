package kiwiband.dnb.ui

import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import kiwiband.dnb.Game
import kiwiband.dnb.InputManager
import kiwiband.dnb.ui.views.InfoView
import kiwiband.dnb.ui.views.MapView
import kiwiband.dnb.ui.views.PlayerView
import kiwiband.dnb.ui.views.layout.BoxLayout
import kiwiband.dnb.ui.views.layout.HorizontalLayout
import kiwiband.dnb.ui.views.layout.VerticalLayout

class App() {
    private val inputManager = InputManager()

    private val rootView = HorizontalLayout(SCREEN_WIDTH, SCREEN_HEIGHT)

    private val terminal = DefaultTerminalFactory().createTerminal()
    private val screen = TerminalScreen(terminal)
    private val game = Game()

    private val renderer = Renderer(screen)

    private fun drawScene() {
        screen.clear();

        renderer.setOffset(0, 0)
        rootView.draw(renderer)

        screen.refresh()
    }

    fun runLoop() {
        screen.startScreen()
        screen.cursorPosition = null


        while (true) {
            drawScene()
            val keyStroke = terminal.readInput()

            if (keyStroke.keyType == KeyType.EOF)
                break

            if (keyStroke.keyType != KeyType.Character)
                continue

            val inputCharacter = keyStroke.character

            if (inputCharacter in HANDLED_CHARACTERS)
                inputManager.handleKey(keyStroke)
        }
    }

    private fun constructScene() {
        val mapView = MapView(game.map, 48, 22)
        val playerView = PlayerView(28,10)
        val infoView = InfoView(28, 10)

        rootView.addChild(BoxLayout(mapView))

        val sidebar = VerticalLayout(30, 24)
        sidebar.addChild(BoxLayout(infoView))
        sidebar.addChild(BoxLayout(playerView))

        rootView.addChild(sidebar)
    }

    fun start() {
        constructScene()

        game.startGame()
        runLoop()
        game.endGame()
    }

    companion object {
        private val HANDLED_CHARACTERS = listOf('w', 'a', 's', 'd')
        private const val SCREEN_WIDTH = 80
        private const val SCREEN_HEIGHT = 24
    }
}

fun main() {
    App().start()
}