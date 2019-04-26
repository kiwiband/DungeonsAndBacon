package kiwiband.dnb.ui.activities

import kiwiband.dnb.events.EventKeyPress
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View

abstract class Activity(private val rootView: View,
                                 private val renderer: Renderer) {
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
        EventKeyPress.dispatcher.pushLayer()
        onStart()
    }

    open fun onStart() {}
    open fun onFinish() {}
}