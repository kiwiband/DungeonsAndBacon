package kiwiband.dnb.ui.views.layout

open class Slot {
    companion object {
        val CONST = Slot()
    }
}

enum class HorizontalAlignment {
    LEFT, CENTER, RIGHT, FILL
}

enum class VerticalAlignment {
    TOP, CENTER, BOTTOM, FILL
}

enum class Size {
    CONSTANT, FILL
}