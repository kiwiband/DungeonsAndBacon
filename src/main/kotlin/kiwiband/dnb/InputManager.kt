package kiwiband.dnb

import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.terminal.Terminal
import kiwiband.dnb.events.EventKeyPress

class InputManager(val terminal: Terminal) {
    private lateinit var handleThread: Thread
    private var isHandle = false

    private var eventKeyId: Int = 0

    fun startKeyHandle() {
        isHandle = true
        handleThread = Thread {
            while(isHandle) {
                val key = terminal.readInput()
                EventKeyPress.dispatcher.run(EventKeyPress(key))
            }
            EventKeyPress.dispatcher.removeHandler(eventKeyId)
        }
        handleThread.start()

        eventKeyId = EventKeyPress.dispatcher.addHandler {
            if (it.key.keyType == KeyType.EOF) {
                stop()
            }
        }
    }

    fun stop() {
        isHandle = false
    }

    fun join() {
        handleThread.join()
    }
}
