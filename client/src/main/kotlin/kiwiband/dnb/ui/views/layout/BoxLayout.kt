package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.util.*
import kiwiband.dnb.ui.views.layout.util.HorizontalAlignment.*

/**
 * Container layout containing only one view in a box.
 */
class BoxLayout(content: View, slot: BoxSlot): Layout<BoxSlot, ChildView<BoxSlot>>(content.width + 2, content.height + 2) {

    constructor(content: View): this(content, BoxSlot())

    init {
        children.add(ChildView(content, slot))
        resize(width, height)
    }

    override fun draw(renderer: Renderer) {
        val content = children.first()
        renderer.drawBox(width, height)

        renderer.withOffset {
            renderer.offset.add(horizontalOffset(content), verticalOffset(content))
            content.view.draw(renderer)
        }
    }

    private fun horizontalOffset(child: ChildView<BoxSlot>) = offset(child.slot.horizontal, width - child.view.width)
    private fun verticalOffset(child: ChildView<BoxSlot>) = offset(child.slot.vertical, height - child.view.height)

    private fun offset(alignment: Alignment, dimDif: Int): Int {
        return when (alignment) {
            Alignment.BEGIN -> 1
            Alignment.CENTER -> Math.max(0, dimDif) / 2 + 1
            Alignment.END -> Math.max(1, dimDif - 1)
            Alignment.FILL -> 1
        }
    }

    override fun resize(width: Int, height: Int) {
        setSize(width, height)
        val hFill = children[0].slot.horizontal == Alignment.FILL
        val vFill = children[0].slot.vertical == Alignment.FILL
        val view = children[0].view
        view.resize(size(hFill, width, view.initWidth), size(vFill, height, view.initHeight))
    }

    private fun size(fill: Boolean, dim: Int, viewDim: Int): Int {
        return if (fill) dim - 2 else Math.min(dim - 2, viewDim)
    }
}

class BoxSlot(
    horizontalAlignment: HorizontalAlignment = FILL,
    verticalAlignment: VerticalAlignment = VerticalAlignment.FILL
) :  Slot() {
    val horizontal: Alignment = Alignment.convert(horizontalAlignment)
    val vertical: Alignment = Alignment.convert(verticalAlignment)
}
