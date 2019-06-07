package kiwiband.dnb.rpc

import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver

class MapUpdateHandler : StreamObserver<Gameservice.JsonString> {
    override fun onNext(value: Gameservice.JsonString) {
        println("Update map: ${value.json}")
    }

    override fun onError(t: Throwable) {
        println("Error: ${t.message}")
    }

    override fun onCompleted() {
        println("Update session completed")
    }
}

fun main() {
    val channel = ManagedChannelBuilder.forAddress("localhost", 12345).usePlaintext().build()
    val gameService = GameServiceGrpc.newBlockingStub(channel)
    val gameServiceAsync = GameServiceGrpc.newStub(channel)
    val id = gameService.connect(Gameservice.Empty.getDefaultInstance()).id
    println("Connected to server! id: $id")
    val idMessage = Gameservice.PlayerId.newBuilder().setId(id).build()
    gameServiceAsync.updateMap(idMessage, MapUpdateHandler())
    for (i in 0..10) {
        gameService.userEvent(Gameservice.JsonString.newBuilder().setJson("{id: $id, i: $i}").build())
        Thread.sleep(1500)
    }
    gameService.disconnect(idMessage)
    Thread.sleep(2000)
}