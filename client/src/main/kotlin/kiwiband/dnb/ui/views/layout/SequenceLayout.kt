package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View


/**
 * Base class for sequential layout, horizontal or vertical.
 * Next view left top corner is aligned on the same axis as the layout's left top corner
 * with a shift based on position in children list.
 * @param isHorizontal whether a layout is horizontal or vertical
 */
abstract class SequenceLayout(width: Int, height: Int, private val isHorizontal: Boolean) : Layout(width, height) {
    fun addChild(view: View) {
        children.add(ChildView(Vec2(0, 0), view))
    }

    override fun draw(renderer: Renderer) {
        renderer.withOffset {
            for (child in children) {
                val view = child.view
                view.draw(renderer)

                if (isHorizontal)
                    renderer.offset.add(Vec2(view.width, 0))
                else
                    renderer.offset.add(Vec2(0, view.height))
            }
        }
    }
}

/**
 * Horizontal sequential layout.
 */
class HorizontalLayout(width: Int, height: Int) : SequenceLayout(width, height, true)

/**
 * Vertical sequential layout.
 */
class VerticalLayout(width: Int, height: Int) : SequenceLayout(width, height, false)
