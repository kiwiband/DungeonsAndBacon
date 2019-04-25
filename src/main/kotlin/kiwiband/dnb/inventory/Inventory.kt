package kiwiband.dnb.inventory

class Inventory(val capacity: Int) {
    private val items = mutableListOf<Item>()

    fun add(item: Item): Boolean {
        if (hasSpace()) {
            items.add(item)
            return true
        }
        return false
    }

    fun items(): List<Item> = items

    fun get(i: Int): Item = items[i]

    fun remove(item: Item) = items.remove(item)

    fun getSize(): Int = items.size
    fun isFull(): Boolean = !hasSpace()
    fun hasSpace(): Boolean = items.size < capacity
    fun isEmpty(): Boolean = items.size == 0
}