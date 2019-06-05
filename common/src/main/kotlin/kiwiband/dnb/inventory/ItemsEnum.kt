package kiwiband.dnb.inventory

import kiwiband.dnb.ASCIIART
import org.json.JSONObject

enum class WeaponItemsEnum(private val item: WeaponItem) {
    DAGGER(WeaponItem(0, 1, "Dagger", "A simple dagger.")),
    IRON_SWORD(WeaponItem(1, 2, "Iron Sword", "Old rusty iron sword.")),
    TACHI(WeaponItem(2, 4, "Tachi", "Perfect japanese katana.")),
    MYSTERIOUS_BACON_AXE(WeaponItem(3, 5, "Mysterious Bacon Axe", "The greatest ancient pure bacon axe.", ASCIIART.AXE));

    fun get() = item.clone()

    companion object {
        val allWeapons = values()
    }
}

enum class ArmorItemsEnum(private val item: ArmorItem) {
    LEATHER_BRASSIERE(ArmorItem(0, 1, "Leather Brassiere", "Funny leather armor. Looks nice.", ASCIIART.BRASSIERE)),
    THORN_ARMOR(ArmorItem(1, -1, "Thorn Armor", "The worst armor ever. Who's create this?")),
    BACON_PLATE(ArmorItem(2, 3, "Bacon Plate", "Excellent armor of neat bacon slices."));

    fun get() = item.clone()

    companion object {
        val allArmors = values()
    }
}

object SpecialItems {
    val dummy = object : Item() {
        override fun getName() = "dummy item"

        override fun getDescription() = "ooops... actually this item shouldn't be in a game"

        override fun clone() = this

        override fun getIcon() = ASCIIART.BAD_ICON

        override fun toJSON(): JSONObject = JSONObject()
    }
}
