package kiwiband.dnb.ui.views.layout.util

open class Node

enum class HorizontalAlignment {
    LEFT, CENTER, RIGHT, FILL
}

enum class VerticalAlignment {
    TOP, CENTER, BOTTOM, FILL
}

enum class Alignment {
    CENTER, FILL;

    fun horizontal() = when (this) {
        CENTER -> HorizontalAlignment.CENTER
        FILL -> HorizontalAlignment.FILL
    }

    fun vertical() = when (this) {
        CENTER -> VerticalAlignment.CENTER
        FILL -> VerticalAlignment.FILL
    }
}

enum class Size {
    CONSTANT, FILL
}