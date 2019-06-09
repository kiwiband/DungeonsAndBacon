package kiwiband.dnb.server

import io.grpc.ServerBuilder
import kiwiband.dnb.events.EventTick
import kiwiband.dnb.map.LocalMap
import java.util.concurrent.locks.ReentrantLock

/**
 * Launcher for the server.
 */
object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val gameLock = ReentrantLock()
        val gameSession = GameSession()
        val gameService = GameServiceImpl(gameSession, gameLock)
        ServerBuilder.forPort(12345).addService(gameService).build().start()

        println("Server started")

        while (true) {
            gameLock.lock()
            EventTick.dispatcher.run(EventTick())
            gameService.sendUpdate()
            gameLock.unlock()
            Thread.sleep(1000)
        }
    }
}
