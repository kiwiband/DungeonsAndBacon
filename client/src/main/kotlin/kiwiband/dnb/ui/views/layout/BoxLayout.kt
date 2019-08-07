package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.util.*

/**
 * Container layout containing only one view in a box.
 */
class BoxLayout(
    content: View,
    slot: BoxSlot = BoxSlot(),
    width: Int = content.width + 2,
    height: Int = content.height + 2
): WrapperLayout(content, slot, width, height) {
    override fun draw(renderer: Renderer) {
        renderer.withOffsetLimited(width, height) {
            renderer.drawBox(width, height)
        }
        val content = children.first()
        renderer.withOffsetLimited(Vec2(1, 1) to Vec2(width - 1, height - 1)) {
            renderer.offset.add(horizontalOffset(content), verticalOffset(content))
            content.view.draw(renderer)
        }
    }
}
class BoxSlot(
    horizontalAlignment: HorizontalAlignment = HorizontalAlignment.FILL,
    verticalAlignment: VerticalAlignment = VerticalAlignment.FILL,
    padding: Padding = Padding()
) : WrapperSlot(horizontalAlignment, verticalAlignment, padding.add(1))
