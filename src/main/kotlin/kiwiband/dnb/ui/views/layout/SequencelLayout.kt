package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View

class SequenceLayout(width: Int, height: Int, private val isHorizontal: Boolean = true) : Layout(width, height) {
    fun addChild(view: View) {
        children.add(ChildView(Vec2M(0, 0), view))
    }

    override fun draw(renderer: Renderer) {
        renderer.withOffset {
            val boxSequenceStep = if (isHorizontal) Vec2M(2, 0) else Vec2M(0, 2)

            for (child in children) {
                val view = child.view
                if (view !is Layout) {
                    renderer.drawBox(view.width, view.height)

                    renderer.withOffset {
                        renderer.offset.add(Vec2M(1, 1))
                        view.draw(renderer)
                    }
                    
                    renderer.offset.add(boxSequenceStep)
                } else {
                    view.draw(renderer)
                }
                if (isHorizontal)
                    renderer.offset.add(Vec2M(view.width, 0))
                else
                    renderer.offset.add(Vec2M(0, view.height))
            }
        }
    }
}