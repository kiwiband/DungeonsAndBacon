package kiwiband.dnb.ui.activities

import kiwiband.dnb.events.EventKeyPress
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View

/**
 * Activity is an entity that takes an entire screen and handles key presses.
 * Its lifecycle is:
 * - Activity created
 * - start() is called from outside of the activity
 * - onStart() gets invoked
 * - finish() is called from inside of the activity
 * - onFinish() gets invoked
 * After finishing the activity, the screen needs to be refreshed from outside.
 */
abstract class Activity(private val renderer: Renderer) {

    /**
     * The root view of the activity. It is always placed in a top left corner.
     */
    private lateinit var rootView: View

    /**
     * Refreshes a screen.
     */
    protected fun drawScene() {
        renderer.clearScreen()
        renderer.setOffset(0, 0)
        rootView.draw(renderer)
        renderer.refreshScreen()
    }

    /**
     * Finishes the activity.
     * DO NOT call this before calling start() (i.e. in constructor)
     */
    protected fun finish() {
        EventKeyPress.dispatcher.popLayer()
        onFinish()
    }

    /**
     * Starts the activity.
     * Does not refresh the screen by default, you can call drawScene() in onStart()
     */
    fun start() {
        rootView = createRootView()
        EventKeyPress.dispatcher.pushLayer()
        onStart()
    }

    /**
     * A function that gets called once on activity start (before onStart())
     * @return the root view of the activity
     */
    abstract fun createRootView(): View

    /**
     * A handler that gets invoked upon activity start.
     */
    open fun onStart() {}

    /**
     * A handler that gets invoked upon activity finish.
     */
    open fun onFinish() {}
}