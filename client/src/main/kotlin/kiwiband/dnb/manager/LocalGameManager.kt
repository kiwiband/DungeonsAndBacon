package kiwiband.dnb.manager

import kiwiband.dnb.Game
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.events.*
import kiwiband.dnb.inventory.Inventory
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.map.MapSaver
import kiwiband.dnb.math.Vec2

class LocalGameManager(private val game: Game): GameManager {

    private val mapSaver = MapSaver()
    private val mapFile = "./maps/saved_map.dnb"
    private lateinit var player: Player

    private fun saveMap(game: Game) {
        mapSaver.saveToFile(game.map, mapFile)
    }

    private fun deleteMap() {
        mapSaver.deleteFile(mapFile)
    }

    override fun getInventory(): Inventory {
        return player.inventory
    }

    override fun getMap(): LocalMap {
        return game.map
    }

    override fun getPlayer(): Player {
        return player
    }

    override fun movePlayer(direction: Vec2) {
        game.eventBus.run(EventMove(direction, 0))
        game.eventBus.run(EventTick())
    }

    override fun useItem(itemNum: Int, playerId: Int) {
        game.eventBus.run(EventUseItem(itemNum, playerId))
    }

    override fun startGame() {
        player = game.getOrCreatePlayer(0)
        game.startGame()
    }

    override fun finishGame(): Boolean {
        val isDead = player.isDead()
        if (player.isDead()) {
            deleteMap()
        } else {
            saveMap(game)
        }
        game.endGame()
        return isDead
    }
}