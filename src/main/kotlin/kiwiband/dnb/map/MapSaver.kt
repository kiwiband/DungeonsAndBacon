package kiwiband.dnb.map

import org.json.JSONObject
import java.io.File
import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Paths

class MapSaver {

    companion object {

        fun saveToFile(map: LocalMap) {
            val file = File(MAP_FILE)
            if (!file.exists()) {
                Files.createDirectory(Paths.get(MAP_FILE).parent)
            }
            File(MAP_FILE).writeText(map.toJSON().toString())
        }

        fun loadFromFile(): LocalMap {
            if (!checkSaved()) {
                throw RuntimeException("No saved maps.")
            }
            return LocalMap(JSONObject(File(MAP_FILE).readText()))
        }

        fun checkSaved() = File(MAP_FILE).exists()

        const val MAP_FILE = "./maps/saved_map.dnd"
    }
}