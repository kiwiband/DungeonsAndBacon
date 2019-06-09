package kiwiband.dnb

import org.ini4j.Ini
import java.io.File
import java.lang.NumberFormatException

/**
 * Launcher for the client.
 */
object Main {
    private const val DEFAULT_HOST = "localhost"
    private const val DEFAULT_PORT = 12345

    @JvmStatic
    fun main(args: Array<String>) {
        var host = DEFAULT_HOST
        var port = DEFAULT_PORT
        val settingsFile = File("settings.ini")
        if (settingsFile.exists()) {
            val settings = Ini(settingsFile)
            settings["server"]?.also { serverProfile ->
                host = serverProfile.getOrDefault("host", DEFAULT_HOST)
                port = serverProfile.get("port")?.let {
                    try {
                        Integer.parseInt(it)
                    } catch (e: NumberFormatException) {
                        println("Wrong port format in settings.ini")
                        DEFAULT_PORT
                    }
                } ?: DEFAULT_PORT
            }
        }
        App(host, port).start()
    }
}