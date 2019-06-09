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
        fillSettings()
        App().start()
    }

    @JvmStatic
    fun fillSettings() {
        val serverFile = File("settings.ini")
        if (serverFile.exists()) {
            val ini = Ini(serverFile)
            ini["server"]?.also { iniGeneral ->
                iniGeneral["host"]?.also { Settings.host = it }
                getIntFromMap(iniGeneral, "port")?.also { Settings.port = it }
            }
            ini["single_player_map"]?.also { iniMap ->
                getIntFromMap(iniMap, "width")?.also { Settings.mapWidth = it }
                getIntFromMap(iniMap, "height")?.also { Settings.mapHeight = it }
                getIntFromMap(iniMap, "mobs_count")?.also { Settings.mobsCount = it }
            }
        } else {
            println("Can't find settings.ini")
        }
    }
}