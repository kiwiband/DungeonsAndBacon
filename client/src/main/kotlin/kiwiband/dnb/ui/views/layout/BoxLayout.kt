package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View

/**
 * Container layout containing only one view in a box.
 */
class BoxLayout(content: View): Layout<Slot, ChildView<Slot>>(content.width + 2, content.height + 2) {

    init {
        children.add(ChildView(content, Slot.CONST))
    }

    override fun draw(renderer: Renderer) {
        val content = children.first()
        renderer.drawBox(width, height)

        renderer.withOffset {
            renderer.offset.add(Vec2(1, 1))
            content.view.draw(renderer)
        }
    }

    override fun resize(width: Int, height: Int) {
        setSize(width, height)
        children[0].view.resize(width - 2, height - 2)
    }

}
