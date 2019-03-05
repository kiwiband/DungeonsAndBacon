package kiwiband.dnb.ui.views

abstract class View(val width: Int, val height: Int) {
    abstract fun to2DArray(): Array<CharArray>
}