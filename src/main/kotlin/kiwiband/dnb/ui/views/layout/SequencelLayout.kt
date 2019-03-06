package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View

abstract class SequenceLayout(width: Int, height: Int, private val isHorizontal: Boolean) : Layout(width, height) {
    fun addChild(view: View) {
        children.add(ChildView(Vec2M(0, 0), view))
    }

    override fun draw(renderer: Renderer) {
        renderer.withOffset {
            for (child in children) {
                val view = child.view
                view.draw(renderer)

                if (isHorizontal)
                    renderer.offset.add(Vec2M(view.width, 0))
                else
                    renderer.offset.add(Vec2M(0, view.height))
            }
        }
    }
}

class HorizontalLayout(width: Int, height: Int) : SequenceLayout(width, height, true)
class VerticalLayout(width: Int, height: Int) : SequenceLayout(width, height, false)