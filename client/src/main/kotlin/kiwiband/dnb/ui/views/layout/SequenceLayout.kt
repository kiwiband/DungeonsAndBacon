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
abstract class SequenceLayout<S : SequenceSlot, Child : ChildView<S>>(
    width: Int, height: Int
) : Layout<S, Child>(width, height) {

    var limits: IntRange? = null

    protected abstract val controller: SequenceLayoutDirectionController<S, Child>

    fun addChild(view: View, slot: S): Child {
        return createChild(view, slot).also {
            children.add(it)
            resize(width, height)
        }
    }

    open fun addChild(view: View) = addChild(view, defaultSlot())

    protected abstract fun createChild(view: View, slot: S): Child

    protected abstract fun defaultSlot(): S

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

    private fun range(): IntRange = max(0, limits?.first ?: 0)..min(children.lastIndex, limits?.last ?: Int.MAX_VALUE)

    override fun resize(width: Int, height: Int) {
        setSize(width, height)
        var knownSize = 0
        var fillChildCount = 0
        for (i in range()) {
            val child = children[i]
            val p = child.slot.padding
            knownSize += controller.chooseDimension(p.horizontal(), p.vertical())
            when (child.slot.size) {
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
        controller.resizeChildren(width, height, fillChildSize, fatChildrenCount)
    }
}


abstract class SequenceSlot(val padding: Padding, val size: Size) : Slot()


abstract class SequenceLayoutDirectionController<S : Slot, Child : ChildView<S>>() {
    abstract fun addOffsetBeforeChildDraw(child: Child, renderer: Renderer)
    abstract fun addOffsetAfterChildDraw(child: Child, renderer: Renderer)
    abstract fun chooseDimension(width: Int, height: Int): Int
    abstract fun resizeChildren(
        width: Int,
        height: Int,
        fillChildSize: Int,
        fatChildrenCount: Int
    )
}