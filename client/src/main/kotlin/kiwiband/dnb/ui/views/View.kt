package kiwiband.dnb.ui.views

import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer
import kotlin.math.max

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

    abstract fun resize(width: Int, height: Int)

    protected open fun setSize(width: Int, height: Int) {
        this.width = max(width, 0)
        this.height = max(height, 0)
    }
}
