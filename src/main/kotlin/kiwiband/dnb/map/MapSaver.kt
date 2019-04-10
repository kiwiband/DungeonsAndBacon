package kiwiband.dnb.map

import org.json.JSONObject
import java.io.File
import java.lang.RuntimeException

/**
 * Class for saving map.
 */
class MapSaver {

    /**
     * Saves a map to file.
     * @param map map to save
     * @param filename name of save file
     */
    fun saveToFile(map: LocalMap, filename: String) {
        val file = File(filename)
        file.parentFile.mkdirs()
        file.writeText(map.toJSON().toString())
    }

    /**
     * Loads a map from a file.
     * @param filename file to load from
     * @return loaded map
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