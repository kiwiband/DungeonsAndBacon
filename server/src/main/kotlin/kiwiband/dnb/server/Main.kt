package kiwiband.dnb.server

import io.grpc.ServerBuilder
import kiwiband.dnb.events.EventTick
import org.ini4j.Ini
import java.io.File
import java.lang.NumberFormatException
import java.util.concurrent.locks.ReentrantLock

/**
 * Launcher for the server.
 */
object Main {
    private const val DEFAULT_PORT = 12345;

    @JvmStatic
    fun main(args: Array<String>) {

        var port = DEFAULT_PORT
        val serverFile = File("server.ini")
        if (serverFile.exists()) {
            try {
                port = Integer.parseInt(Ini(serverFile).get("general", "port") ?: DEFAULT_PORT.toString())
            } catch (e: NumberFormatException) {
                println("Wrong port format in server.ini")
            }
        } else {
            println("Can't find server.ini")
        }

        println("Port has been set: $port")

        val gameLock = ReentrantLock()
        val gameSession = GameSession()
        val gameService = GameServiceImpl(gameSession, gameLock)
        ServerBuilder.forPort(port).addService(gameService).build().start()

        println("Server started")

        while (true) {
            gameLock.lock()
            gameSession.game.eventBus.run(EventTick())
            gameService.sendUpdate()
            gameLock.unlock()
            Thread.sleep(1000)
        }
    }
}
