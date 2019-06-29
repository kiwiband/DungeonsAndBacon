package kiwiband.dnb.server

import io.grpc.ServerBuilder
import kiwiband.dnb.Settings
import kiwiband.dnb.Settings.getIntFromMap
import kiwiband.dnb.events.EventTick
import org.ini4j.Ini
import java.io.File
import java.util.concurrent.locks.ReentrantLock

/**
 * Launcher for the server.
 */
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        fillSettings()

        println("Port has been set: ${ServerSettings.port}")

        val gameLock = ReentrantLock()
        val gameSession = GameSession()
        val gameService = GameServiceImpl(gameSession, gameLock)
        ServerBuilder.forPort(ServerSettings.port).addService(gameService).build().start()

        println("Server started")

        while (true) {
            gameLock.lock()
            gameSession.game.eventBus.run(EventTick())
            gameService.sendUpdate()
            gameLock.unlock()
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
