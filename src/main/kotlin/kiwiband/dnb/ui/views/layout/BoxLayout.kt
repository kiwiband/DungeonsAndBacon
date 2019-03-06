package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View

class BoxLayout(content: View): Layout(content.width + 2, content.height + 2) {

    init {
        children.add(ChildView(Vec2M(1, 1), content))
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