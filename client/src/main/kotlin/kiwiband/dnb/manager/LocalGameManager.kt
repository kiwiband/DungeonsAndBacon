package kiwiband.dnb.manager

import kiwiband.dnb.Game
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.events.EventMove
import kiwiband.dnb.events.EventTick
import kiwiband.dnb.inventory.Inventory
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.map.MapSaver
import kiwiband.dnb.math.Vec2

class LocalGameManager(private val game: Game): GameManager {

    private val mapSaver = MapSaver()
    private val mapFile = "./maps/saved_map.dnb"

    private fun saveMap(game: Game) {
        mapSaver.saveToFile(game.map, mapFile)
    }

    private fun deleteMap() {
        mapSaver.deleteFile(mapFile)
    }

    override fun getInventory(): Inventory {
        return game.player.inventory
    }

    override fun getMap(): LocalMap {
        return game.map
    }

    override fun getPlayer(): Player {
        return game.player
    }

    override fun setOnGameStateChange(gameHandler: (Game) -> Unit) { }

    override fun movePlayer(direction: Vec2) {
        EventMove.dispatcher.run(EventMove(direction))
        EventTick.dispatcher.run(EventTick())
    }

    override fun useItem(itemNum: Int) {
        game.player.useItem(itemNum)
    }

    override fun startGame() {
        game.startGame()
    }

    override fun finishGame(): Boolean {
        val isDead = game.player.isDead()
        if (game.player.isDead()) {
            deleteMap()
        } else {
            saveMap(game)
        }
        game.endGame()
        return isDead
    }
}