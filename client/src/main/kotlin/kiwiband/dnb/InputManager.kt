package kiwiband.dnb

import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.terminal.Terminal
import kiwiband.dnb.events.*
import java.util.concurrent.locks.ReentrantLock

/**
 * Class for handling keys in another thread
 */
class InputManager(
    private val terminal: Terminal,
    private val eventLock: ReentrantLock,
    private val eventBus: EventBus
) {
    private lateinit var handleThread: Thread
    private var isHandle = false

    /**
     * Starts handling key presses.
     * Stops upon receiving EOF or stop() method call.
     */
    fun startKeyHandle() {
        isHandle = true
        handleThread = Thread {
            while(isHandle) {
                val key = terminal.readInput()
                eventLock.lock()
                eventBus.run(EventKeyPress(key))
                eventLock.unlock()
                if (key.keyType == KeyType.EOF)
                    break
            }
        }
        handleThread.start()

        eventBus.eventKeyPress.addPermanentHandler {
            if (it.key.keyType == KeyType.EOF) {
                eventBus.run(EventGameOver())
            }
        }
    }

    /**
     * Stops handling key presses.
     */
    fun stop() {
        isHandle = false
    }

    /**
     * Blocks execution until key press handling is stopped.
     */
    fun join() {
        handleThread.join()
    }
}
