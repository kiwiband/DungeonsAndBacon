package kiwiband.dnb

object ClientSettings {
    var multiplayer: Boolean = Default.MULTIPLAYER
    var mapFile: String = "./maps/saved_map.dnb"

    object Default {
        const val MULTIPLAYER = false
    }
}