package kiwiband.dnb.map

import org.json.JSONObject
import java.io.File
import java.lang.RuntimeException

class MapSaver {

    fun saveToFile(map: LocalMap, filename: String) {
        val file = File(filename)
        file.parentFile.mkdirs()
        file.writeText(map.toJSON().toString())
    }

    fun loadFromFile(filename: String): LocalMap {
        if (!checkSaved(filename)) {
            throw RuntimeException("No saved maps.")
        }
        return LocalMap.loadMap(JSONObject(File(filename).readText()))
    }

    fun checkSaved(filename: String) = File(filename).exists()
}