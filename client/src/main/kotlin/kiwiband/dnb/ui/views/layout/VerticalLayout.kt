package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.util.Slot
import kiwiband.dnb.ui.views.layout.util.HorizontalAlignment
import kiwiband.dnb.ui.views.layout.util.HorizontalAlignment.*
import kiwiband.dnb.ui.views.layout.util.Padding
import kiwiband.dnb.ui.views.layout.util.Size
import kotlin.math.max


/**
 * Vertical sequential layout.
 */
class VerticalLayout(width: Int, height: Int) :
    SequenceLayout<VerticalNode, Slot<VerticalNode>>(width, height) {

    override val controller = VerticalSlotController(width, height, children)

    override fun createSlot(view: View, node: VerticalNode) = Slot(view, node)

    override fun defaultNode() = VerticalNode()
}

class VerticalSlotController<S : Slot<VerticalNode>>(
    width: Int,
    height: Int,
    private val children: List<S>
) : SequenceSlotController<VerticalNode, S>(width, height) {
    override fun addOffsetBeforeChildDraw(child: S, renderer: Renderer) {
        val p = child.node.padding
        renderer.offset.add(horizontalOffset(child.node.alignment, child.view, p), p.top)
    }

    override fun addOffsetAfterChildDraw(child: S, renderer: Renderer) {
        val p = child.node.padding
        renderer.offset.add(-horizontalOffset(child.node.alignment, child.view, p), p.bottom + child.view.height)
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
            val childWidth = when (child.node.alignment) {
                FILL -> width - child.node.padding.horizontal()
                else -> child.view.initWidth
            }
            when (child.node.size) {
                Size.CONSTANT -> view.resize(childWidth, view.height)
                Size.FILL -> {
                    val size = if (counter-- > 0) fillChildSize + 1 else fillChildSize
                    view.resize(childWidth, size)
                }
            }
        }
    }
}


class VerticalNode(
    val alignment: HorizontalAlignment = FILL,
    padding: Padding = Padding(),
    verticalSize: Size = Size.CONSTANT
) : SequenceNode(padding, verticalSize)