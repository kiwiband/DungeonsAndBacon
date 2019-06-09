package kiwiband.dnb.ui.activities

import kiwiband.dnb.ui.AppContext
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
abstract class Activity<T>(protected val context: AppContext,
                           private val callback: (T) -> Unit) {
    private val renderer = context.renderer
    private val lock = Object()
    private var result: T? = null
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

    protected fun close() {
        context.eventBus.eventKeyPress.popLayer()
        val activities = context.activities
        activities.removeLast()
        activities.peekLast()?.drawScene()
    }

    /**
     * Finishes the activity.
     * DO NOT call this before calling start() (i.e. in constructor)
     */
    protected fun finish(result: T) {
        close()
        onFinish(result)
        callback(result)
    }

    /**
     * Starts the activity.
     * Does not refresh the screen by default, you can call drawScene() in onStart()
     */
    fun start() {
        rootView = createRootView()
        context.eventBus.eventKeyPress.pushLayer()
        context.activities.addLast(this)
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
    open fun onFinish(result: T) {}
}