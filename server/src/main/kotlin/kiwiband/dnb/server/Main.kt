package kiwiband.dnb.server

import io.grpc.ServerBuilder
import kiwiband.dnb.map.LocalMap


fun main() {
    val map = LocalMap.generateMap(40, 40)
    val mapJson = map.toJSON().toString()

    val gameService = GameServiceImpl(mapJson)
    ServerBuilder.forPort(12345).addService(gameService).build().start()

    println("Server started")

    while (true) {
        gameService.sendUpdate(mapJson)
        Thread.sleep(1000)
    }
}
