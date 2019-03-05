package kiwiband.dnb.ui

import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import kiwiband.dnb.InputManager
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.ui.views.BoxView

class TerminalApp(private val map: LocalMap,
                  private val inputManager: InputManager,
                  private val width: Int = 80, private val height: Int = 24): App(map, inputManager) {

    private val rootView = BoxView(width, height)
    private val terminal = DefaultTerminalFactory().createTerminal()
    private val screen = TerminalScreen(terminal)

    override fun drawScene() {
        screen.clear()
        val serialized = rootView.to2DArray()

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

    companion object {
        private val HANDLED_CHARACTERS = listOf('w', 'a', 's', 'd')
    }
}
