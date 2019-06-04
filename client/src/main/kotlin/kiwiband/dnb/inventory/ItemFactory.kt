package kiwiband.dnb.inventory

import kiwiband.dnb.inventory.WeaponItemsEnum.*
import kiwiband.dnb.inventory.ArmorItemsEnum.*
import kotlin.random.Random

object ItemFactory {
    val weaponLibrary : List<WeaponItem> = listOf(
        DAGGER,
        IRON_SWORD,
        TACHI,
        MYSTERIOUS_BACON_AXE
    ).map { it.get() }

    val armorLibrary : List<ArmorItem> = listOf(
        LEATHER_BRASSIERE,
        THORN_ARMOR,
        BACON_PLATE
    ).map { it.get() }

    fun getRandomWeapon(): WeaponItem = weaponLibrary[Random.nextInt(weaponLibrary.size)].clone()

    fun getRandomArmor(): ArmorItem = armorLibrary[Random.nextInt(armorLibrary.size)].clone()

    fun getRandomItem(): Item {
        return when (Random.nextInt(2)) {
            0 -> getRandomArmor()
            else -> getRandomWeapon()
        }
    }
}