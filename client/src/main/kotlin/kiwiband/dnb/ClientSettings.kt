package kiwiband.dnb

object ClientSettings {
    var mapFile: String = Default.MAP_FILE
    var playerId: String = Default.PLAYER_ID
    var sessionId: String = ""


    object Default {
        const val PLAYER_ID = "@user"
        const val MAP_FILE = "./maps/saved_map.dnb"
    }
}