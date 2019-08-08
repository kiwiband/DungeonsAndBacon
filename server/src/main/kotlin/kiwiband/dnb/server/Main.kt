package kiwiband.dnb.server

import io.grpc.ServerBuilder
import kiwiband.dnb.Settings
import kiwiband.dnb.Settings.getIntFromMap
import kiwiband.dnb.events.EventTick
import org.ini4j.Ini
import java.io.File

/**
 * Launcher for the server.
 */
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        fillSettings()

        println("Port has been set: ${ServerSettings.port}")

        val gameService = GameServiceImpl()
        ServerBuilder.forPort(ServerSettings.port).addService(gameService).build().start()

        println("Server started")

        while (true) {
            gameService.sessions.forEach { entry ->
                val session = entry.value
                session.lock()
                session.game.eventBus.run(EventTick())
                gameService.sendUpdate(session)
                session.unlock()
            }
            Thread.sleep(ServerSettings.tickTime)
        }
    }

    @JvmStatic
    fun fillSettings() {
        val serverFile = File("server.ini")
        if (serverFile.exists()) {
            val ini = Ini(serverFile)
            ini["general"]?.also { iniGeneral ->
                getIntFromMap(iniGeneral, "port")?.also { ServerSettings.port = it }
                getIntFromMap(iniGeneral, "tick_time")?.also { ServerSettings.tickTime = it.toLong() }
            }
            ini["map"]?.also { iniMap ->
                getIntFromMap(iniMap, "width")?.also { Settings.mapWidth = it }
                getIntFromMap(iniMap, "height")?.also { Settings.mapHeight = it }
                getIntFromMap(iniMap, "mobs_count")?.also { Settings.mobsCount = it }
            }
        } else {
            println("Can't find server.ini")
        }
    }
}
