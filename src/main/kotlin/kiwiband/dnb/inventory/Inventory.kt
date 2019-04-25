package kiwiband.dnb.inventory

class Inventory(val size: Int) {
    private val items = mutableListOf<Item>()

    fun add(item: Item): Boolean {
        if (hasSpace()) {
            items.add(item)
            return true
        }
        return false
    }

    fun get(): List<Item> = items

    fun remove(item: Item) = items.remove(item)

    fun isFull() = !hasSpace()

    fun hasSpace() = items.size < size
}