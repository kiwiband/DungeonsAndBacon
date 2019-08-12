package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.util.*
import kotlin.math.max
import kotlin.math.min

open class WrapperLayout(
    content: View,
    slot: WrapperSlot = WrapperSlot(),
    width: Int = content.width + slot.padding.horizontal(),
    height: Int = content.height + slot.padding.vertical()
): Layout<WrapperSlot, ChildView<WrapperSlot>>(width, height) {

    init {
        children.add(ChildView(content, slot))
        resize(width, height)
    }

    override fun draw(renderer: Renderer) {
        val content = children.first()
        renderer.withOffsetLimited(width, height) {
            renderer.offset.add(horizontalOffset(content), verticalOffset(content))
            content.view.draw(renderer)
        }
    }

    protected fun horizontalOffset(child: ChildView<WrapperSlot>): Int {
        val p = child.slot.padding
        val freeWidth = width - child.view.width
        return when (child.slot.horizontal) {
            HorizontalAlignment.LEFT -> p.left
            HorizontalAlignment.CENTER -> max(0, freeWidth - p.horizontal()) / 2 + p.left
            HorizontalAlignment.RIGHT -> max(1, freeWidth - p.right)
            HorizontalAlignment.FILL -> p.left
        }
    }

    protected fun verticalOffset(child: ChildView<WrapperSlot>): Int {
        val p = child.slot.padding
        val freeHeight = height - child.view.height
        return when (child.slot.vertical) {
            VerticalAlignment.TOP -> p.top
            VerticalAlignment.CENTER -> max(0, freeHeight - p.vertical()) / 2 + p.top
            VerticalAlignment.BOTTOM -> max(1, freeHeight - p.bottom)
            VerticalAlignment.FILL -> p.top
        }
    }

    override fun updateSize() {
        val slot = children[0].slot
        val hFill = slot.horizontal == HorizontalAlignment.FILL
        val vFill = slot.vertical == VerticalAlignment.FILL
        val view = children[0].view
        view.resize(
            size(hFill, width - slot.padding.horizontal(), view.initWidth),
            size(vFill, height - slot.padding.vertical(), view.initHeight)
        )
    }

    private fun size(fill: Boolean, dim: Int, viewDim: Int): Int {
        return if (fill) dim else min(dim, viewDim)
    }
}


open class WrapperSlot(
    val horizontal: HorizontalAlignment = HorizontalAlignment.FILL,
    val vertical: VerticalAlignment = VerticalAlignment.FILL,
    val padding: Padding = Padding()
) :  Slot()