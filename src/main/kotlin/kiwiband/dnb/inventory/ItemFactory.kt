package kiwiband.dnb.inventory

import kiwiband.dnb.ui.ASCIIART
import kotlin.random.Random

object ItemFactory {
    val weaponLibrary : List<WeaponItem> = listOf(
        WeaponItem(1, "Dagger", "A simple dagger."),
        WeaponItem(2, "Iron Sword", "Old rusty iron sword."),
        WeaponItem(4, "Tachi", "Perfect japanese katana."),
        WeaponItem(5, "Mysterious Bacon Axe", "The greatest ancient pure bacon axe.", ASCIIART.AXE)
    )

    val armorLibrary : List<ArmorItem> = listOf(
        ArmorItem(1, "Leather Brassiere", "Funny leather armor. Looks nice."),
        ArmorItem(-1, "Thorn Armor", "The worst armor ever. Who's create this?"),
        ArmorItem(3, "Bacon Plate", "Excellent armor of neat bacon slices.")
    )

    fun getRandomWeapon(): WeaponItem = weaponLibrary[Random.nextInt(weaponLibrary.size)].clone()

    fun getRandomArmor(): ArmorItem = armorLibrary[Random.nextInt(armorLibrary.size)].clone()

    fun getRandomItem(): Item {
        return when (Random.nextInt(2)) {
            0 -> getRandomArmor()
            else -> getRandomWeapon()
        }
    }
}