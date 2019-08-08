package kiwiband.dnb.server

import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import kiwiband.dnb.rpc.GameServiceGrpc
import kiwiband.dnb.rpc.Gameservice
import kiwiband.dnb.rpc.Gameservice.JoinResult.Status as JoinStatus
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * An implementation of the GRPC game service
 */
class GameServiceImpl :
    GameServiceGrpc.GameServiceImplBase() {

    private val updateObservers = ConcurrentHashMap<String, StreamObserver<Gameservice.JsonString>>()
    val sessions = hashMapOf<String, GameSession>()
    private val players = ConcurrentHashMap<String, String>()

    override fun createSession(request: Gameservice.PlayerId, responseObserver: StreamObserver<Gameservice.SessionId>) {
        val id = UUID.randomUUID().toString()
        val gameSession = GameSession()
        sessions[id] = gameSession
        responseObserver.onNext(Gameservice.SessionId.newBuilder().setId(id).build())
        responseObserver.onCompleted()
    }

    override fun joinSession(request: Gameservice.JoinRequest, responseObserver: StreamObserver<Gameservice.JoinResult>) {
        val result = Gameservice.JoinResult.newBuilder()
        result.status = if (!sessions.containsKey(request.sessionId)) {
            JoinStatus.NO_SUCH_SESSION
        } else if (players.containsKey(request.playerId)) {
            JoinStatus.ALREADY_CONNECTED
        } else {
            addPlayerToSession(request.playerId, request.sessionId)
            JoinStatus.OK
        }

        responseObserver.onNext(result.build())
        responseObserver.onCompleted()
    }

    private fun addPlayerToSession(playerId: String, sessionId: String) {
        val session = sessions[sessionId]!!
        session.lock()
        session.addNewPlayer(playerId)
        players[playerId] = sessionId
        session.unlock()
    }

    /**
     * Remove a player from the map;
     * Close and remove the player's observer
     */
    override fun disconnect(request: Gameservice.PlayerId, responseObserver: StreamObserver<Gameservice.Empty>) {

        val session = getPlayerSession(request.id) ?: return // todo send response

        println("Player ${request.id} left the game")

        session.lock()
        session.removePlayer(request.id)
        players.remove(request.id)
        session.unlock()

        updateObservers.remove(request.id)?.onCompleted() // todo concurrency issue
        responseObserver.onNext(Gameservice.Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }

    /**
     * Handle a user event
     */
    override fun userEvent(request: Gameservice.UserEvent, responseObserver: StreamObserver<Gameservice.Empty>) {
        println("User event happened (player id: ${request.playerId}): ${request.json}")

        val session = getPlayerSession(request.playerId) ?: return // todo send response

        session.lock()
        session.game.eventBus.runFromJSON(JSONObject(request.json))
        session.unlock()

        responseObserver.onNext(Gameservice.Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }

    private fun getPlayerSession(playerId: String): GameSession? {
        val sessionId = players[playerId] ?: return null
        return sessions[sessionId]!!
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
    fun sendUpdate(session: GameSession) {
        session.lock()
        val mapJson = session.game.map.toString()
        session.unlock()

        session.currentMap.set(mapJson)
        val message = Gameservice.JsonString.newBuilder().setJson(mapJson).build()
        val removedObservers = mutableListOf<String>()
        updateObservers.forEach { (id, observer) ->
            try {
                observer.onNext(message)
            } catch (e: StatusRuntimeException) {
                e.printStackTrace()
                println("Player $id disconnected")
                removedObservers.add(id)
            }
        }
        session.lock()
        removedObservers.forEach { // todo test what happens on a sudden disconnect
            updateObservers.remove(it)
            session.removePlayer(it)
            players.remove(it)
        }
        session.unlock()
    }
}
