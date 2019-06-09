package kiwiband.dnb

import java.io.File

/**
 * Launcher for the game.
 */
object Main {

    private const val DEFAULT_HOST = "localhost"
    private const val DEFAULT_PORT = 12345

    @JvmStatic
    fun main(args: Array<String>) {
        val confFile = File("server.conf")
        var host = DEFAULT_HOST
        var port = DEFAULT_PORT
        if (confFile.exists()) {
            confFile.useLines { lins ->
                val lines = lins.toList()
                host = lines.elementAt(0)
                port = lines.elementAt(1).toInt()
            }
        }
        App(host, port).start()
    }
}