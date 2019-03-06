package kiwiband.dnb.ui

import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import kiwiband.dnb.InputManager
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.views.*
import kiwiband.dnb.ui.views.layout.SequenceLayout

class TerminalApp(map: LocalMap,
                  private val inputManager: InputManager,
                  width: Int = 80, height: Int = 24): App(map, inputManager) {

    private val mapView = MapView(map, 48, 22)
    private val playerView = PlayerView(28,10)
    private val infoView = InfoView(28, 10)

    private val rootView = SequenceLayout(width, height)

    private val terminal = DefaultTerminalFactory().createTerminal()
    private val screen = TerminalScreen(terminal)

    override fun drawScene() {
        screen.clear()
        rootView.draw(screen, Vec2M(0, 0))
     
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

    fun start() {
        rootView.addChild(mapView)

        val verticalView = SequenceLayout(30, 24, false)
        verticalView.addChild(infoView)
        verticalView.addChild(playerView)
        rootView.addChild(verticalView)

        runLoop()
    }

    companion object {
        private val HANDLED_CHARACTERS = listOf('w', 'a', 's', 'd')
    }
}
