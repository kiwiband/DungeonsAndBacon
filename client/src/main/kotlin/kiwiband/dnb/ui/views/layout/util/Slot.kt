package kiwiband.dnb.ui.views.layout.util

open class Slot

enum class HorizontalAlignment {
    LEFT, CENTER, RIGHT, FILL
}

enum class VerticalAlignment {
    TOP, CENTER, BOTTOM, FILL
}

enum class Alignment {
    BEGIN, CENTER, END, FILL;

    companion object {
        fun convert(alignment: HorizontalAlignment) = when(alignment) {
            HorizontalAlignment.LEFT -> BEGIN
            HorizontalAlignment.CENTER -> CENTER
            HorizontalAlignment.RIGHT -> END
            HorizontalAlignment.FILL -> FILL
        }

        fun convert(alignment: VerticalAlignment) = when(alignment) {
            VerticalAlignment.TOP -> BEGIN
            VerticalAlignment.CENTER -> CENTER
            VerticalAlignment.BOTTOM -> END
            VerticalAlignment.FILL -> FILL
        }
    }
}

enum class Size {
    CONSTANT, FILL
}