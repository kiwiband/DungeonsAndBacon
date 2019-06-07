package kiwiband.dnb.rpc

import io.grpc.ServerBuilder
import kiwiband.dnb.map.LocalMap


fun main() {
    val gameService = GameServiceImpl()
    ServerBuilder.forPort(12345).addService(gameService).build().start()

    val map = LocalMap.generateMap(40, 40)

    val json = map.toJSON().toString()

    while (true) {
        gameService.sendUpdate(json)
        Thread.sleep(1000)
    }
}
