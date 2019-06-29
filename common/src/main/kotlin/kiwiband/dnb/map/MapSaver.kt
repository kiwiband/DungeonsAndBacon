package kiwiband.dnb.map

import org.json.JSONObject
import java.io.File
import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Class for saving map.
 */
class MapSaver {

    fun deleteFile(filename: String) {
        if (checkSaved(filename)) {
            Files.delete(Paths.get(filename))
        }
    }

    fun saveToFile(map: LocalMap, filename: String) {
        val file = File(filename)
        file.parentFile.mkdirs()
        file.writeText(map.toString())
    }

    /**
     * @throws RuntimeException if a map was not found.
     */
    fun loadFromFile(filename: String): LocalMap {
        if (!checkSaved(filename)) {
            throw RuntimeException("No saved maps.")
        }
        return LocalMap.loadMap(JSONObject(File(filename).readText()))
    }

    /**
     * Checks if a map is saved.
     * @param filename file to load from
     * @return if a map with such name exists.
     */
    fun checkSaved(filename: String) = File(filename).exists()
}