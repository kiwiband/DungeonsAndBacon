package kiwiband.dnb.manager

import kiwiband.dnb.Game
import kiwiband.dnb.ServerCommunicationManager
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.events.EventItemUsed
import kiwiband.dnb.events.EventMove
import kiwiband.dnb.events.EventTick
import kiwiband.dnb.events.EventUpdateMap
import kiwiband.dnb.inventory.Inventory
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2

class MultiplayerGameManager(private val comm: ServerCommunicationManager, private val id: Int, var game: Game): GameManager {

    private fun updateGame(map: LocalMap) {
        game = Game(map, id)
        EventTick.dispatcher.run(EventTick())
    }

    init {
        EventUpdateMap.dispatcher.addHandler { updateGame(it.newMap) }
    }

    override fun movePlayer(direction: Vec2) {
        comm.sendEvent(EventMove(direction))
    }

    override fun useItem(itemNum: Int) {
        comm.sendEvent(EventItemUsed(itemNum))
    }

    override fun finishGame(): Boolean {
        return false
    }

    override fun startGame() {

    }

    override fun getMap(): LocalMap {
        return game.map
    }

    override fun getPlayer(): Player {
        return game.player
    }

    override fun getInventory(): Inventory {
        return game.player.inventory
    }

}