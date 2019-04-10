package kiwiband.dnb.ui.views

import kiwiband.dnb.ui.Renderer

/**
 * View base class.
 * @param width view width
 * @param height view height
 */
abstract class View(val width: Int, val height: Int) {
    /**
     * Renders a view on a renderer
     * @param renderer renderer to draw on
     */
    abstract fun draw(renderer: Renderer)
}
