package kiwiband.dnb.inventory

abstract class Item {
    abstract fun getName(): String
    abstract fun getDescription(): String
    abstract fun clone(): Item
}