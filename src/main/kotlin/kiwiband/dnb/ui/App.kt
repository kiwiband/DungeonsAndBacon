package kiwiband.dnb.ui

import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import kiwiband.dnb.math.Vec2

class App {
    private var characterX = 5
    private var characterY = 5
    private val terminal = DefaultTerminalFactory().createTerminal()
    private val screen = TerminalScreen(terminal)

    private fun drawScene() {
        screen.clear()
        screen.setCharacter(
            characterX,
            characterY,
            TextCharacter(
                '@',
                TextColor.ANSI.BLACK,
                TextColor.ANSI.WHITE))
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

            moveCharacter(movement)
        }
    }

    private fun moveCharacter(movement: Vec2) {
        val newX = characterX + movement.x
        val newY = characterY + movement.y
        if (newX >= 0 && newX < screen.terminalSize.columns)
            characterX = newX
        if (newY >= 0 && newY < screen.terminalSize.rows)
            characterY = newY
    }


}

fun main(args: Array<String>) {
    App().runLoop()
}
