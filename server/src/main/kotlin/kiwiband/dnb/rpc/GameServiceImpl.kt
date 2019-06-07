package kiwiband.dnb.rpc

import io.grpc.stub.StreamObserver
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class GameServiceImpl : GameServiceGrpc.GameServiceImplBase() {

    private val currentPlayerId = AtomicInteger()
    private val updateObservers = ConcurrentHashMap<Int, StreamObserver<Gameservice.JsonString>>()
    private val currentMap = AtomicReference<Gameservice.JsonString>()

    override fun connect(request: Gameservice.Empty, responseObserver: StreamObserver<Gameservice.PlayerId>) {
        val id = currentPlayerId.getAndIncrement()
        responseObserver.onNext(Gameservice.PlayerId.newBuilder().setId(id).build())
        println("Player $id joined the game")
        responseObserver.onCompleted()
    }

    override fun disconnect(request: Gameservice.PlayerId, responseObserver: StreamObserver<Gameservice.Empty>) {
        println("Player ${request.id} left the game")
        updateObservers.remove(request.id)?.onCompleted()
        responseObserver.onNext(Gameservice.Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }

    override fun userEvent(request: Gameservice.JsonString, responseObserver: StreamObserver<Gameservice.Empty>) {
        println("User event happened: ${request.json}")
        responseObserver.onNext(Gameservice.Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }

    override fun updateMap(request: Gameservice.PlayerId, responseObserver: StreamObserver<Gameservice.JsonString>) {
        updateObservers[request.id] = responseObserver
        responseObserver.onNext(currentMap.get())
    }

    fun sendUpdate(json: String) {
        val message = Gameservice.JsonString.newBuilder().setJson(json).build()
        currentMap.set(message)
        updateObservers.forEach { (_, observer) ->
            observer.onNext(message)
        }
    }
}
