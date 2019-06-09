package kiwiband.dnb.server

import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import kiwiband.dnb.events.Event
import kiwiband.dnb.rpc.GameServiceGrpc
import kiwiband.dnb.rpc.Gameservice
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock

/**
 * An implementation of the GRPC game service
 */
class GameServiceImpl(private val gameSession: GameSession, private val gameLock: ReentrantLock) :
    GameServiceGrpc.GameServiceImplBase() {

    private val currentPlayerId = AtomicInteger()
    private val updateObservers = ConcurrentHashMap<Int, StreamObserver<Gameservice.JsonString>>()
    private val currentMap = AtomicReference<String>(gameSession.game.map.toString())

    /**
     * Register a new player, spawn it on the map and return the player id
     */
    override fun connect(request: Gameservice.Empty, responseObserver: StreamObserver<Gameservice.InitialState>) {
        val id = currentPlayerId.getAndIncrement()

        gameLock.lock()
        gameSession.addNewPlayer(id)
        currentMap.set(gameSession.game.map.toString())
        gameLock.unlock()

        responseObserver.onNext(Gameservice.InitialState.newBuilder().setPlayerId(id).setMapJson(currentMap.get()).build())
        println("Player $id joined the game")
        responseObserver.onCompleted()
    }

    /**
     * Remove a player from the map;
     * Close and remove the player's observer
     */
    override fun disconnect(request: Gameservice.PlayerId, responseObserver: StreamObserver<Gameservice.Empty>) {
        println("Player ${request.id} left the game")

        gameLock.lock()
        gameSession.removePlayer(request.id)
        gameLock.unlock()

        updateObservers.remove(request.id)?.onCompleted()
        responseObserver.onNext(Gameservice.Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }

    /**
     * Handle a user event
     */
    override fun userEvent(request: Gameservice.UserEvent, responseObserver: StreamObserver<Gameservice.Empty>) {
        println("User event happened (player id: ${request.playerId}): ${request.json}")

        gameLock.lock()
        Event.runFromJSON(JSONObject(request.json))
        gameLock.unlock()

        responseObserver.onNext(Gameservice.Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }

    /**
     * Register a user's updates observer
     */
    override fun updateMap(request: Gameservice.PlayerId, responseObserver: StreamObserver<Gameservice.JsonString>) {
        updateObservers[request.id] = responseObserver
    }

    /**
     * Send the up to date map to all registered observers
     */
    fun sendUpdate() {
        gameLock.lock()
        val mapJson = gameSession.game.map.toString()
        gameLock.unlock()

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
