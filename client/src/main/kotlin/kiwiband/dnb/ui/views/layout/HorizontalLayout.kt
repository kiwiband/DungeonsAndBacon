package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.util.Slot
import kiwiband.dnb.ui.views.layout.util.VerticalAlignment.*
import kiwiband.dnb.ui.views.layout.util.Padding
import kiwiband.dnb.ui.views.layout.util.Size
import kiwiband.dnb.ui.views.layout.util.VerticalAlignment
import kotlin.math.max

/**
 * Horizontal sequential layout.
 */
class HorizontalLayout(width: Int, height: Int)
    : SequenceLayout<HorizontalNode, Slot<HorizontalNode>>(width, height) {

    override val controller = HorizontalSlotController(width, height, children)

    override fun createSlot(view: View, node: HorizontalNode) = Slot(view, node)

    override fun defaultNode() = HorizontalNode()
}


class HorizontalSlotController<S : Slot<HorizontalNode>>(
    width: Int,
    height: Int,
    private val children: List<S>
) : SequenceSlotController<HorizontalNode, S>(width, height) {

    override fun addOffsetBeforeChildDraw(child: S, renderer: Renderer) {
        val p = child.node.padding
        renderer.offset.add(p.left, verticalOffset(child.node.alignment, child.view, p))
    }

    override fun addOffsetAfterChildDraw(child: S, renderer: Renderer) {
        val p = child.node.padding
        renderer.offset.add(p.right + child.view.width, -verticalOffset(child.node.alignment, child.view, p))
    }

    private fun verticalOffset(alignment: VerticalAlignment, view: View, p: Padding): Int {
        return when (alignment) {
            TOP -> p.top
            CENTER -> max(0, p.top + (height - view.height - p.vertical()) / 2)
            BOTTOM -> max(0, height - view.height - p.bottom)
            FILL -> p.top
        }
    }

    override fun chooseDimension(width: Int, height: Int) = width

    override fun resizeChildren(range: IntRange, fillChildSize: Int, fatChildrenCount: Int) {
        var counter = fatChildrenCount
        for (i in range) {
            val child = children[i]
            val view = child.view
            val childHeight = when (child.node.alignment) {
                FILL -> height - child.node.padding.vertical()
                else -> child.view.initHeight
            }
            when (child.node.size) {
                Size.CONSTANT -> view.resize(view.initWidth, childHeight)
                Size.FILL -> {
                    val size = if (counter-- > 0) fillChildSize + 1 else fillChildSize
                    view.resize(size, childHeight)
                }
            }
        }
    }
}


open class HorizontalNode(
    val alignment: VerticalAlignment = FILL,
    padding: Padding = Padding(),
    horizontalSize: Size = Size.CONSTANT
) : SequenceNode(padding, horizontalSize)
