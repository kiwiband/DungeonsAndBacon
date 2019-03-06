package kiwiband.dnb

import com.googlecode.lanterna.input.KeyStroke
import kiwiband.dnb.events.EventMove
import kiwiband.dnb.events.EventTick
import kiwiband.dnb.math.Vec2M

class InputManager {
    fun handleKey(keyStroke: KeyStroke) {
        val movement = when (keyStroke.character) {
            'w' -> Vec2M(0, -1)
            'a' -> Vec2M(-1, 0)
            's' -> Vec2M(0, 1)
            'd' -> Vec2M(1, 0)
            else -> null
        }
        movement?.also { EventMove.dispatcher.run(EventMove(it)) }

        EventTick.dispatcher.run(EventTick())
    }
}