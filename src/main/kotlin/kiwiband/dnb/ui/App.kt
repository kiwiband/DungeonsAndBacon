package kiwiband.dnb.ui

import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.views.BoxView

class App(private val width: Int = 80, private val height: Int = 24) {
    private val rootView = BoxView(width, height)
    private val terminal = DefaultTerminalFactory().createTerminal()
    private val screen = TerminalScreen(terminal)

    private fun drawScene() {
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

            if (keyStroke.keyType == KeyType.EOF)
                break

            val movement = when (keyStroke?.character) {
                'w' -> Vec2(0, -1)
                'a' -> Vec2(-1, 0)
                's' -> Vec2(0, 1)
                'd' -> Vec2(1, 0)
                else -> Vec2(0, 0)
            }
        }
    }
}

fun main(args: Array<String>) {
    App().runLoop()
}
