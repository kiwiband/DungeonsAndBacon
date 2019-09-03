package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.util.*
import kotlin.math.max
import kotlin.math.min


/**
 * Base class for sequential layout, horizontal or vertical.
 * Next view left top corner is aligned on the same axis as the layout's left top corner
 * with a shift based on position in children list.
 */
abstract class SequenceLayout<N : SequenceNode, S : Slot<N>>(
    width: Int, height: Int
) : Layout<N, S>(width, height) {

    var limits: IntRange = 0..Int.MAX_VALUE

    protected abstract val controller: SequenceSlotController<N, S>

    open fun add(child: S): S {
        children.add(child)
        updateSize()
        return child
    }

    fun add(view: View, node: N = defaultNode()): S {
        return add(createSlot(view, node))
    }

    protected abstract fun createSlot(view: View, node: N): S

    protected abstract fun defaultNode(): N

    override fun draw(renderer: Renderer) {
        renderer.withOffsetLimited(width, height) {
            for (i in range()) {
                val child = children[i]
                controller.addOffsetBeforeChildDraw(child, renderer)
                child.view.draw(renderer)
                controller.addOffsetAfterChildDraw(child, renderer)
            }
        }
    }

    private fun range(): IntRange = max(0, limits.first)..min(children.lastIndex, limits.last)


    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)
        controller.width = width
        controller.height = height
    }

    override fun updateSize() {
        var knownSize = 0
        var fillChildCount = 0
        val range = range()
        for (i in range) {
            val child = children[i]
            val p = child.node.padding
            knownSize += controller.chooseDimension(p.horizontal(), p.vertical())
            when (child.node.size) {
                Size.CONSTANT -> {
                    knownSize += controller.chooseDimension(child.view.initWidth, child.view.initHeight)
                }
                Size.FILL -> fillChildCount++
            }
        }
        val mainSize = controller.chooseDimension(width, height)
        val retainedSize = max(mainSize - knownSize, 0)
        val fillChildSize = if (fillChildCount == 0) 0 else retainedSize / fillChildCount
        val fatChildrenCount = if (fillChildCount == 0) 0 else retainedSize % fillChildCount
        controller.resizeChildren(range, fillChildSize, fatChildrenCount)
    }
}


abstract class SequenceNode(val padding: Padding, val size: Size) : Node()


abstract class SequenceSlotController<N : Node, S : Slot<N>>(var width: Int, var height: Int) {
    abstract fun addOffsetBeforeChildDraw(child: S, renderer: Renderer)
    abstract fun addOffsetAfterChildDraw(child: S, renderer: Renderer)
    abstract fun chooseDimension(width: Int, height: Int): Int
    abstract fun resizeChildren(range: IntRange, fillChildSize: Int, fatChildrenCount: Int)
}