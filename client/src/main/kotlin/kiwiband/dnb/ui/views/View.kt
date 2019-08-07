package kiwiband.dnb.ui.views

import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer

/**
 * View base class.
 * @param width view width
 * @param height view height
 */
abstract class View(val initWidth: Int, val initHeight: Int) {
    var width = initWidth
    var height = initHeight

    constructor(sizes: Vec2) : this(sizes.x, sizes.y)

    /**
     * Renders a view on a renderer
     * @param renderer renderer to draw on
     */
    abstract fun draw(renderer: Renderer)

    open fun resize(width: Int, height: Int) {}

    protected fun setSize(width: Int, height: Int) {
        this.width = Math.max(width, 0)
        this.height = Math.max(height, 0)
    }
}
