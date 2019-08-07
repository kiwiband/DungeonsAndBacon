package kiwiband.dnb.ui.views.layout

import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View


/**
 * Base class for sequential layout, horizontal or vertical.
 * Next view left top corner is aligned on the same axis as the layout's left top corner
 * with a shift based on position in children list.
 * @param isHorizontal whether a layout is horizontal or vertical
 */
abstract class SequenceLayout<S : SequenceSlot>(
    width: Int, height: Int,
    private val isHorizontal: Boolean
) : Layout<S, SequenceChildView<S>>(width, height) {

    fun addChild(view: View, slot: S): SequenceChildView<S> {
        return SequenceChildView(view, slot).also {
            children.add(it)
            resize(width, height)
        }
    }

    fun addChild(view: View) = addChild(view, defaultSlot())

    abstract fun defaultSlot(): S

    override fun draw(renderer: Renderer) {
        renderer.withOffset {
            for (child in children) {
                addOffsetBeforeChildDraw(child, renderer)
                child.view.draw(renderer)
                addOffsetAfterChildDraw(child, renderer)
            }
        }
    }

    protected abstract fun addOffsetBeforeChildDraw(child: SequenceChildView<S>, renderer: Renderer)
    protected abstract fun addOffsetAfterChildDraw(child: SequenceChildView<S>, renderer: Renderer)

    override fun resize(width: Int, height: Int) {
        setSize(width, height)
        var knownSize = 0
        var fillChildCount = 0
        for (child in children) {
            val p = child.slot.padding
            knownSize += chooseDirection(p.horizontal(), p.vertical())
            when (child.slot.size) {
                Size.CONSTANT -> {
                    knownSize += chooseDirection(child.view.initWidth, child.view.initHeight)
                }
                Size.FILL -> fillChildCount++
            }
        }
        val mainSize = chooseDirection(width, height)
        val retainedSize = Math.max(mainSize - knownSize, 0)
        val fillChildSize = if (fillChildCount == 0) 0 else retainedSize / fillChildCount
        val fatChildrenCount = if (fillChildCount == 0) 0 else retainedSize % fillChildCount
        resizeChildren(width, height, fillChildSize, fatChildrenCount)
    }

    protected abstract fun chooseDirection(width: Int, height: Int): Int

    protected abstract fun resizeChildren(
        width: Int,
        height: Int,
        fillChildSize: Int,
        fatChildrenCount: Int
    )
}

class SequenceChildView<T : SequenceSlot>(view: View, slot: T) : ChildView<T>(view, slot)

abstract class SequenceSlot(val padding: Padding, val size: Size) : Slot()
