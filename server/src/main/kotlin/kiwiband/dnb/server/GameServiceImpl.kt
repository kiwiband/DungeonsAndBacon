package kiwiband.dnb.server

import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import kiwiband.dnb.rpc.GameServiceGrpc
import kiwiband.dnb.rpc.Gameservice
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class GameServiceImpl(mapJson: String, val gameSession: GameSession) : GameServiceGrpc.GameServiceImplBase() {

    private val currentPlayerId = AtomicInteger()
    private val updateObservers = ConcurrentHashMap<Int, StreamObserver<Gameservice.JsonString>>()
    private val currentMap = AtomicReference<String>(mapJson)

    override fun connect(request: Gameservice.Empty, responseObserver: StreamObserver<Gameservice.InitialState>) {
        val id = currentPlayerId.getAndIncrement()
        gameSession.addNewPlayer(id)
        currentMap.set(gameSession.game.map.toJSON().toString())
        responseObserver.onNext(Gameservice.InitialState.newBuilder().setPlayerId(id).setMapJson(currentMap.get()).build())
        println("Player $id joined the game")
        responseObserver.onCompleted()
    }

    override fun disconnect(request: Gameservice.PlayerId, responseObserver: StreamObserver<Gameservice.Empty>) {
        println("Player ${request.id} left the game")
        gameSession.removePlayer(request.id)
        updateObservers.remove(request.id)?.onCompleted()
        responseObserver.onNext(Gameservice.Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }

    override fun userEvent(request: Gameservice.UserEvent, responseObserver: StreamObserver<Gameservice.Empty>) {
        println("User event happened (player id: ${request.playerId}): ${request.json}")
        responseObserver.onNext(Gameservice.Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }

    override fun updateMap(request: Gameservice.PlayerId, responseObserver: StreamObserver<Gameservice.JsonString>) {
        updateObservers[request.id] = responseObserver
    }

    fun sendUpdate(mapJson: String) {
        currentMap.set(mapJson)
        val message = Gameservice.JsonString.newBuilder().setJson(mapJson).build()
        val removedObservers = mutableListOf<Int>()
        updateObservers.forEach { (id, observer) ->
            try {
                observer.onNext(message)
            } catch (e: StatusRuntimeException) {
                e.printStackTrace()
                println("Player $id removed")
                removedObservers.add(id)
            }
        }
        removedObservers.forEach { updateObservers.remove(it) }
    }
}
