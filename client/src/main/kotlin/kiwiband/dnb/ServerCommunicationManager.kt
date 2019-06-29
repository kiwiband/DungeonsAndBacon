package kiwiband.dnb

import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import kiwiband.dnb.events.Event
import kiwiband.dnb.events.EventBus
import kiwiband.dnb.events.EventUpdateMap
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.rpc.GameServiceGrpc
import kiwiband.dnb.rpc.Gameservice
import org.json.JSONObject
import java.util.concurrent.locks.ReentrantLock

/**
 * A client-side of GRPC game service
 */
class ServerCommunicationManager(
    private val host: String,
    private val port: Int,
    private val eventLock: ReentrantLock,
    private val eventBus: EventBus
) {

    private lateinit var gameService: GameServiceGrpc.GameServiceBlockingStub
    private lateinit var gameServiceAsync: GameServiceGrpc.GameServiceStub
    private val mapdUpdateHandler = MapUpdateHandler()
    private var id = -1

    /**
     * Connect to a game server and get a player id and the current game state
     */
    fun connect(): Pair<Int, LocalMap> {
        println("Connecting to server at $host:$port")
        val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
        gameService = GameServiceGrpc.newBlockingStub(channel)
        gameServiceAsync = GameServiceGrpc.newStub(channel)
        val state = gameService.connect(Gameservice.Empty.getDefaultInstance())
        id = state.playerId

        println("Connected to server with id: $id")
        val idMessage = Gameservice.PlayerId.newBuilder().setId(id).build()

        gameServiceAsync.updateMap(idMessage, mapdUpdateHandler)
        return id to LocalMap.loadMap(JSONObject(state.mapJson))
    }

    /**
     * Send an even to the game server
     */
    fun sendEvent(event: Event) {
        gameService.userEvent(
            Gameservice.UserEvent.newBuilder().setPlayerId(id).setJson(event.toJSON().toString()).build()
        )
    }

    /**
     * Disconnect from the game server
     */
    fun disconnect() {
        gameService.disconnect(Gameservice.PlayerId.newBuilder().setId(id).build())
    }

    /**
     * State update handler
     */
    private inner class MapUpdateHandler : StreamObserver<Gameservice.JsonString> {

        /**
         * Get the current game state
         */
        override fun onNext(value: Gameservice.JsonString) {
            eventLock.lock()
            eventBus.run(
                EventUpdateMap(LocalMap.loadMap(JSONObject(value.json)))
            )
            eventLock.unlock()
        }

        override fun onError(t: Throwable) {
            println("Error getting map update: ${t.message}")
        }

        override fun onCompleted() {
            println("Game session completed")
        }
    }
}