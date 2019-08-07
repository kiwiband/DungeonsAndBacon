package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.util.ChildView
import kiwiband.dnb.ui.views.layout.util.VerticalAlignment.*
import kiwiband.dnb.ui.views.layout.util.Padding
import kiwiband.dnb.ui.views.layout.util.Size
import kiwiband.dnb.ui.views.layout.util.VerticalAlignment

/**
 * Horizontal sequential layout.
 */
class HorizontalLayout(width: Int, height: Int) : SequenceLayout<HorizontalSlot>(width, height, true) {

    override fun defaultSlot() = HorizontalSlot()

    override fun addOffsetBeforeChildDraw(child: ChildView<HorizontalSlot>, renderer: Renderer) {
        val p = child.slot.padding
        renderer.offset.add(p.left, verticalOffset(child.slot.alignment, child.view, p))
    }

    override fun addOffsetAfterChildDraw(child: ChildView<HorizontalSlot>, renderer: Renderer) {
        val p = child.slot.padding
        renderer.offset.add(p.right + child.view.width, -verticalOffset(child.slot.alignment, child.view, p))
    }

    private fun verticalOffset(alignment: VerticalAlignment, view: View, p: Padding): Int {
        return when (alignment) {
            TOP -> p.top
            CENTER -> Math.max(0, p.top + (height - view.height - p.vertical()) / 2)
            BOTTOM -> Math.max(0, height - view.height - p.bottom)
            FILL -> p.top
        }
    }

    override fun chooseDimension(width: Int, height: Int) = width

    override fun resizeChildren(
        width: Int,
        height: Int,
        fillChildSize: Int,
        fatChildrenCount: Int
    ) {
        var counter = fatChildrenCount
        for (child in children) {
            val view = child.view
            val childHeight = when (child.slot.alignment) {
                FILL -> height - child.slot.padding.vertical()
                else -> child.view.initHeight
            }
            when (child.slot.size) {
                Size.CONSTANT -> view.resize(view.initWidth, childHeight)
                Size.FILL -> {
                    val size = if (counter-- > 0) fillChildSize + 1 else fillChildSize
                    view.resize(size, childHeight)
                }
            }
        }
    }
}


class HorizontalSlot(
    padding: Padding = Padding(),
    horizontalSize: Size = Size.CONSTANT,
    val alignment: VerticalAlignment = FILL
) : SequenceSlot(padding, horizontalSize)