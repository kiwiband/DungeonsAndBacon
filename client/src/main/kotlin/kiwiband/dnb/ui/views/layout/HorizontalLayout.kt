package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.util.ChildView
import kiwiband.dnb.ui.views.layout.util.VerticalAlignment.*
import kiwiband.dnb.ui.views.layout.util.Padding
import kiwiband.dnb.ui.views.layout.util.Size
import kiwiband.dnb.ui.views.layout.util.VerticalAlignment
import kotlin.math.max

/**
 * Horizontal sequential layout.
 */
class HorizontalLayout(width: Int, height: Int)
    : SequenceLayout<HorizontalSlot, ChildView<HorizontalSlot>>(width, height) {

    override val controller = SequenceLayoutHorizontalController(width, height, children)

    override fun createChild(view: View, slot: HorizontalSlot) = ChildView(view, slot)

    override fun defaultSlot() = HorizontalSlot()

    override fun resize(width: Int, height: Int) {
        controller.width = width
        controller.height = height
        super.resize(width, height)
    }
}


class SequenceLayoutHorizontalController<Child : ChildView<HorizontalSlot>>(
    var width: Int,
    var height: Int,
    private val children: List<Child>
) : SequenceLayoutDirectionController<HorizontalSlot, Child>() {

    override fun addOffsetBeforeChildDraw(child: Child, renderer: Renderer) {
        val p = child.slot.padding
        renderer.offset.add(p.left, verticalOffset(child.slot.alignment, child.view, p))
    }

    override fun addOffsetAfterChildDraw(child: Child, renderer: Renderer) {
        val p = child.slot.padding
        renderer.offset.add(p.right + child.view.width, -verticalOffset(child.slot.alignment, child.view, p))
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


open class HorizontalSlot(
    padding: Padding = Padding(),
    horizontalSize: Size = Size.CONSTANT,
    val alignment: VerticalAlignment = FILL
) : SequenceSlot(padding, horizontalSize)
