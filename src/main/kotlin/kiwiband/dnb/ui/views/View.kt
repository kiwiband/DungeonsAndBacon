package kiwiband.dnb.ui.views

import com.googlecode.lanterna.screen.Screen
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.Renderer

abstract class View(val width: Int, val height: Int) {
    abstract fun draw(renderer: Renderer)
}