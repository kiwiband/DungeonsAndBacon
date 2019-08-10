package kiwiband.dnb.server

import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import kiwiband.dnb.rpc.GameServiceGrpc
import kiwiband.dnb.rpc.Gameservice
import kiwiband.dnb.rpc.Gameservice.ConnectResult.Status as ConnectStatus
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * An implementation of the GRPC game service
 */
class GameServiceImpl :
    GameServiceGrpc.GameServiceImplBase() {

    private val updateObservers = ConcurrentHashMap<String, StreamObserver<Gameservice.JsonString>>()
    val sessions = ConcurrentHashMap<String, GameSession>()
    private val players = ConcurrentHashMap<String, String>()

    override fun createSession(request: Gameservice.PlayerId,
                               responseObserver: StreamObserver<Gameservice.ConnectResult>) {
        val sessionId = UUID.randomUUID().toString()
        val gameSession = GameSession()

        // we don't need to use locks here because the session is not visible to other threads yet
        gameSession.addNewPlayer(request.id)
        players[request.id] = sessionId
        val response = buildConnectResult(ConnectStatus.OK, sessionId, gameSession.game.map.toString())
        sessions[sessionId] = gameSession
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    override fun joinSession(request: Gameservice.JoinRequest,
                             responseObserver: StreamObserver<Gameservice.ConnectResult>) {
        val response = if (!sessions.containsKey(request.sessionId)) {
            buildConnectResult(ConnectStatus.NO_SUCH_SESSION, "", "")
        } else if (players.containsKey(request.playerId)) {
            buildConnectResult(ConnectStatus.ALREADY_CONNECTED, "", "")
        } else {
            addPlayerToSession(request.playerId, request.sessionId)
            val sessionId = request.sessionId
            val session = sessions[sessionId]!!
            session.lock()
            val mapJson = session.game.map.toString()
            session.unlock()
            buildConnectResult(ConnectStatus.OK, sessionId, mapJson)
        }
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    private fun buildConnectResult(status: Gameservice.ConnectResult.Status,
                                   sessionId: String,
                                   mapJson: String): Gameservice.ConnectResult {
        val connectResult = Gameservice.ConnectResult.newBuilder()
        connectResult.status = status
        connectResult.sessionId = sessionId
        connectResult.mapJson = mapJson
        return connectResult.build()
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
