package kiwiband.dnb.map

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class MapSaverTest {

    @JvmField
    @Rule
    val folder = TemporaryFolder()

    @Test
    fun checkSavedTest() {
        val file = folder.newFile()
        file.delete()
        val saver = MapSaver()
        assertFalse(saver.checkSaved(file.canonicalPath))
        saver.saveToFile(LocalMap(10, 10), file.canonicalPath)
        assertTrue(saver.checkSaved(file.canonicalPath))
    }

    @Test
    fun deleteFileTest() {
        val file = folder.newFile()
        val saver = MapSaver()
        saver.saveToFile(LocalMap(10, 10), file.canonicalPath)
        saver.deleteFile(file.canonicalPath)
        assertFalse(saver.checkSaved(file.canonicalPath))
    }

    @Test
    fun saveLoadTest() {
        val expected = LocalMap.generateMap(40, 40)
        val file = folder.newFile()
        val saver = MapSaver()
        saver.saveToFile(expected, file.canonicalPath)
        val actual = saver.loadFromFile(file.canonicalPath)
        assertEquals(expected.toJSON().toString(), actual.toJSON().toString())
    }
}