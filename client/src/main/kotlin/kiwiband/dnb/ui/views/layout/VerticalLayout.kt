package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.util.ChildView
import kiwiband.dnb.ui.views.layout.util.HorizontalAlignment
import kiwiband.dnb.ui.views.layout.util.HorizontalAlignment.*
import kiwiband.dnb.ui.views.layout.util.Padding
import kiwiband.dnb.ui.views.layout.util.Size
import kotlin.math.max


/**
 * Vertical sequential layout.
 */
class VerticalLayout(width: Int, height: Int) :
    SequenceLayout<VerticalSlot, ChildView<VerticalSlot>>(width, height) {

    override val controller = SequenceLayoutVerticalController(width, height, children)

    override fun createChild(view: View, slot: VerticalSlot) = ChildView(view, slot)

    override fun defaultSlot() = VerticalSlot()

    override fun resize(width: Int, height: Int) {
        controller.width = width
        controller.height = height
        super.resize(width, height)
    }
}

class SequenceLayoutVerticalController<Child : ChildView<VerticalSlot>>(
    var width: Int,
    var height: Int,
    private val children: List<Child>
) :
    SequenceLayoutDirectionController<VerticalSlot, Child>() {
    override fun addOffsetBeforeChildDraw(child: Child, renderer: Renderer) {
        val p = child.slot.padding
        renderer.offset.add(horizontalOffset(child.slot.alignment, child.view, p), p.top)
    }

    override fun addOffsetAfterChildDraw(child: Child, renderer: Renderer) {
        val p = child.slot.padding
        renderer.offset.add(-horizontalOffset(child.slot.alignment, child.view, p), p.bottom + child.view.height)
    }

    private fun horizontalOffset(alignment: HorizontalAlignment, view: View, p: Padding): Int {
        return when (alignment) {
            LEFT -> p.left
            CENTER -> max(0, p.left + (width - view.width - p.horizontal()) / 2)
            RIGHT -> max(0, width - view.width - p.right)
            FILL -> p.left
        }
    }

    override fun chooseDimension(width: Int, height: Int) = height

    override fun resizeChildren(range: IntRange, fillChildSize: Int, fatChildrenCount: Int) {
        var counter = fatChildrenCount
        for (i in range) {
            val child = children[i]
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