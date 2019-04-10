package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View

/**
 * Container layout containing only one view in a box.
 */
class BoxLayout(content: View): Layout(content.width + 2, content.height + 2) {

    init {
        children.add(ChildView(Vec2(1, 1), content))
    }

    override fun draw(renderer: Renderer) {
        val content = children.first()
        renderer.drawBox(width, height)

        renderer.withOffset {
            renderer.offset.add(content.offset)
            content.view.draw(renderer)
        }
    }

}
