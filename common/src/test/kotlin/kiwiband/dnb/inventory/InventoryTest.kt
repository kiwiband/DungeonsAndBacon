package kiwiband.dnb.inventory

import org.junit.Assert.*
import org.junit.Test

class InventoryTest {

    @Test
    fun capacityTest() {
        val inventory = Inventory(2)
        assertTrue(inventory.add(ArmorItem(1, 1, "", "")))
        assertTrue(inventory.add(ArmorItem(1, 1, "", "")))
        assertFalse(inventory.add(ArmorItem(1, 1, "", "")))
    }

    @Test
    fun hasSpaceTest() {
        val inventory = Inventory(2)
        inventory.add(ArmorItem(1, 1, "", ""))
        assertTrue(inventory.hasSpace())
        inventory.add(ArmorItem(1, 1, "", ""))
        assertFalse(inventory.hasSpace())
    }

    @Test
    fun getTest() {
        val inventory = Inventory(10)
        val items = listOf<Item>(
            ArmorItem(1, 1, "asd", "xcv"),
            WeaponItem(3, 1, "sdf", "qwe"),
            ArmorItem(1, 1, "asd", "xcv"),
            WeaponItem(3, 1, "sdf", "qwe")
        )
        for (item in items) {
            inventory.add(item)
        }
        assertArrayEquals(items.toTypedArray(), inventory.items.toTypedArray())
    }
}