package kiwiband.dnb.ui.activities

import kiwiband.dnb.events.EventKeyPress
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View

abstract class Activity(private val renderer: Renderer) {
    private lateinit var rootView: View

    protected fun drawScene() {
        renderer.clearScreen()
        renderer.setOffset(0, 0)
        rootView.draw(renderer)
        renderer.refreshScreen()
    }

    protected fun finish() {
        EventKeyPress.dispatcher.popLayer()
        onFinish()
    }

    fun start() {
        rootView = createRootView()
        EventKeyPress.dispatcher.pushLayer()
        onStart()
    }

    abstract fun createRootView(): View
    open fun onStart() {}
    open fun onFinish() {}
}