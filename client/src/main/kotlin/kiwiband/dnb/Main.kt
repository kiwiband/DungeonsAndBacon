package kiwiband.dnb

import kiwiband.dnb.Settings.getIntFromMap
import org.ini4j.Ini
import java.io.File

/**
 * Launcher for the client.
 */
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        fillSettings(args)
        App().start()
    }

    @JvmStatic
    fun fillSettings(args: Array<String>) {
        val settingsFile = File("settings.ini")
        if (settingsFile.exists()) {
            val ini = Ini(settingsFile)
            ini["server"]?.also { iniGeneral ->
                iniGeneral["host"]?.also { Settings.host = it }
                getIntFromMap(iniGeneral, "port")?.also { Settings.port = it }
            }
            ini["map"]?.also { iniMap ->
                getIntFromMap(iniMap, "width")?.also { Settings.mapWidth = it }
                getIntFromMap(iniMap, "height")?.also { Settings.mapHeight = it }
                getIntFromMap(iniMap, "mobs_count")?.also { Settings.mobsCount = it }
            }
            ini["general"]?.also { iniGeneral ->
                iniGeneral["multiplayer"]?.toLowerCase()?.also {
                    ClientSettings.multiplayer = it == "true"
                }
                iniGeneral["session_id"]?.also {
                    ClientSettings.sessionId = it
                }
            }
            ini["player"]?.also { iniPlayer ->
                iniPlayer["player_id"]?.also {
                    ClientSettings.playerId = it
                }
            }
        } else {
            println("Can't find settings.ini")
        }
        if (args.isNotEmpty() && args.first() == "multiplayer") {
            ClientSettings.multiplayer = true
        }
    }
}