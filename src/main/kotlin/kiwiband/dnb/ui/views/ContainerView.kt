package kiwiband.dnb.ui.views

data class ChildView(val offsetX: Int, val offsetY: Int, val view: View)

class ContainerView(width: Int, height: Int) : View(width, height) {

    private val children = mutableListOf<ChildView>()

    fun addChild(offsetX: Int, offsetY: Int, view: View) {
        children.add(ChildView(offsetX, offsetY, view))
    }

    override fun to2DArray(): Array<CharArray> {
        val result = Array(height) { CharArray(width) { ' ' } }

        children.forEach { child ->
            val childArray = child.view.to2DArray()
            childArray.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, character ->
                    result[child.offsetY + rowIndex][child.offsetX + columnIndex] = character
                }
            }
        }

        return result
    }
}
/*
fun printView(view: View) {
    view.to2DArray().forEach {
        it.forEach { print(it) }
        println()
    }
}

fun main() {
    val view = ContainerView(10, 10)
    view.addChild(0, 0, BoxView(3, 3))
    view.addChild(2, 2, BoxView(2, 2))
    printView(view)
}
        */