package kiwiband.dnb.manager

import kiwiband.dnb.ASCIIART
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
        eventBus.updateMap.addHandler { updateMap(it.newMap) }
        recolorPlayers()
    }

    private fun updateMap(map: LocalMap) {
        localMap = map
        val player = map.findPlayer(playerId)
        if (player == null) {
            eventBus.run(EventGameOver())
            return
        }
        localPlayer = player
        recolorPlayers()
        map.updateLit()
        eventBus.run(EventTick())
    }

    private fun recolorPlayers() {
        localMap.actors.forEach {
            if (it is Player && it.playerId != playerId) {
                it.appearance.color = ASCIIART.WHITE
                it.appearance.char = ('0' + it.playerId % 10)
            }
        }
    }

    override fun movePlayer(direction: Vec2) {
        comm.sendEvent(EventMove(direction, playerId))
    }

    override fun useItem(itemNum: Int, playerId: Int) {
        comm.sendEvent(EventUseItem(itemNum, playerId))
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