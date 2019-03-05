package kiwiband.dnb.ui

import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import kiwiband.dnb.InputManager
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.ui.MapDrawUtil.addBorders
import kiwiband.dnb.ui.views.BoxView
import kiwiband.dnb.ui.views.ContainerView
import kiwiband.dnb.ui.views.MapView

class TerminalApp(map: LocalMap,
                  private val inputManager: InputManager,
                  private val width: Int = 80, private val height: Int = 24): App(map, inputManager) {

    private val mapView = MapView(map, 48, 22)
    private val playerView = BoxView(30,11)
    private val enemyView = BoxView(30, 10)

    private val rootView = ContainerView(width, height)

    private val terminal = DefaultTerminalFactory().createTerminal()
    private val screen = TerminalScreen(terminal)

    override fun drawScene() {
        screen.clear()
        val serialized = rootView.to2DArray()

        addBorders(serialized)

        serialized.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, character ->
                screen.setCharacter(
                    columnIndex,
                    rowIndex,
                    TextCharacter(
                        character,
                        TextColor.ANSI.DEFAULT,
                        TextColor.ANSI.DEFAULT)
                )
            }
        }

        screen.refresh()
    }

    fun runLoop() {
        screen.startScreen()
        screen.cursorPosition = null

        while (true) {
            drawScene()
            val keyStroke = terminal.readInput()
            println(keyStroke)
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
        rootView.addChild(1, 1, mapView)
        rootView.addChild(mapView.width + 2, 1, enemyView)
        rootView.addChild(mapView.width + 2, enemyView.height + 2, playerView)

        runLoop()
    }

    companion object {
        private val HANDLED_CHARACTERS = listOf('w', 'a', 's', 'd')
    }
}
