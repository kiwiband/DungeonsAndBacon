package kiwiband.dnb.manager

import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.inventory.Inventory
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2

interface GameManager {
    fun movePlayer(direction: Vec2)

    fun useItem(itemNum: Int, playerId: String)

    fun finishGame(): Boolean

    fun startGame()

    fun getMap(): LocalMap

    fun getPlayer(): Player
}