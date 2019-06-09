package kiwiband.dnb.manager

import kiwiband.dnb.ServerCommunicationManager
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.events.*
import kiwiband.dnb.inventory.Inventory
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2

class MultiplayerGameManager(
    private val comm: ServerCommunicationManager,
    var playerId: Int,
    var localMap: LocalMap,
    private val eventBus: EventBus
) : GameManager {
    var localPlayer: Player = localMap.findPlayer(playerId) ?: localMap.spawnPlayer(playerId)

    init {
        eventBus.eventUpdateMap.addHandler { updateMap(it.newMap) }
    }

    private fun updateMap(map: LocalMap) {
        localMap = map
        val player = map.findPlayer(playerId)
        if (player == null) {
            eventBus.run(EventGameOver())
            return
        }
        localPlayer = player
        eventBus.run(EventTick())
    }

    override fun movePlayer(direction: Vec2) {
        comm.sendEvent(EventMove(direction, playerId))
    }

    override fun useItem(itemNum: Int) {
        comm.sendEvent(EventItemUsed(itemNum))
    }

    override fun finishGame(): Boolean {
        return true
    }

    override fun startGame() {

    }

    override fun getMap(): LocalMap {
        return localMap
    }

    override fun getPlayer(): Player {
        return localPlayer
    }

    override fun getInventory(): Inventory {
        return localPlayer.inventory
    }

}