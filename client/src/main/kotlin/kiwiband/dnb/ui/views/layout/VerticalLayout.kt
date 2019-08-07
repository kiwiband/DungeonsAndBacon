package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.HorizontalAlignment.*


/**
 * Vertical sequential layout.
 */
class VerticalLayout(width: Int, height: Int) : SequenceLayout<VerticalSlot>(width, height, false) {

    override fun defaultSlot() = VerticalSlot()

    override fun addOffsetBeforeChildDraw(child: SequenceChildView<VerticalSlot>, renderer: Renderer) {
        val p = child.slot.padding
        renderer.offset.add(horizontalOffset(child.slot.alignment, child.view, p), p.top)
    }

    override fun addOffsetAfterChildDraw(child: SequenceChildView<VerticalSlot>, renderer: Renderer) {
        val p = child.slot.padding
        renderer.offset.add(-horizontalOffset(child.slot.alignment, child.view, p), p.bottom + child.view.height)
    }

    private fun horizontalOffset(alignment: HorizontalAlignment, view: View, p: Padding): Int {
        return when (alignment) {
            LEFT -> p.left
            CENTER -> Math.max(0, p.left + p.top + (width - view.width) / 2 - p.horizontal())
            RIGHT -> Math.max(0, width - view.width - p.right)
            FILL -> p.left
        }
    }

    override fun chooseDirection(width: Int, height: Int) = height

    override fun resizeChildren(
        width: Int,
        height: Int,
        fillChildSize: Int,
        fatChildrenCount: Int
    ) {
        var counter = fatChildrenCount
        for (child in children) {
            val view = child.view
            val childWidth = when (child.slot.alignment) {
                FILL -> width - child.slot.padding.horizontal()
                else -> child.view.initWidth
            }
            when (child.slot.size) {
                Size.CONSTANT -> view.resize(childWidth, view.height)
                Size.FILL -> {
                    val size = if (counter-- > 0) fillChildSize + 1 else fillChildSize
                    view.resize(childWidth, size)
                }
            }
        }
    }
}

class VerticalSlot(
    padding: Padding = Padding(),
    verticalSize: Size = Size.CONSTANT,
    val alignment: HorizontalAlignment = FILL
) : SequenceSlot(padding, verticalSize)