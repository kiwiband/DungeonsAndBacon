package kiwiband.dnb.ui.activities

import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import com.sun.org.apache.xpath.internal.operations.Bool
import kiwiband.dnb.Game
import kiwiband.dnb.InputManager
import kiwiband.dnb.events.*
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.map.MapSaver
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.App
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.GameOverView
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.BoxLayout

class GameActivity(private val game: Game, rootView: View, renderer: Renderer): Activity(rootView, renderer) {
    private val mapSaver = MapSaver()
    private val mapFile = "./maps/saved_map.dnb"

    private fun handleMoveKeys(keyStroke: KeyStroke) {
        if (keyStroke.keyType != KeyType.Character) return
        when (keyStroke.character) {
            'w', 'ц' -> Vec2(0, -1)
            'a', 'ф' -> Vec2(-1, 0)
            's', 'ы' -> Vec2(0, 1)
            'd', 'в' -> Vec2(1, 0)
            else -> null
        }?.also {
            EventMove.dispatcher.run(EventMove(it))
            tick()
        }
    }

    private fun tick() {
        EventTick.dispatcher.run(EventTick())
        drawScene()
    }

    private fun saveMap(game: Game) {
        mapSaver.saveToFile(game.map, mapFile)
    }

    private fun deleteMap() {
        mapSaver.deleteFile(mapFile)
    }

    override fun onStart() {
        drawScene()

        EventKeyPress.dispatcher.addHandler {
            if (it.key.keyType == KeyType.Escape) {
                EventGameOver.dispatcher.run(EventGameOver())
            }
        }

        EventKeyPress.dispatcher.addHandler { handleMoveKeys(it.key) }

        EventGameOver.dispatcher.addHandler {
            val isDead = game.player.isDead()
            if (isDead) {
                deleteMap()
            } else {
                saveMap(game)
            }
            game.endGame()
            finish()
            EventGameActivityFinished.dispatcher.run(EventGameActivityFinished(isDead))
        }
/*

        val eventInventoryOpenId = EventKeyPress.dispatcher.addHandler {
            if (it.key.keyType == KeyType.Character) {
                when (it.key.character) {
                    'i', 'ш' -> {} //openInventory(game)
                }
            }
        }
*/

        game.startGame()
    }
}

class EventGameActivityFinished(result: Boolean): EventActivityFinished<Boolean>(result) {
    companion object {
        val dispatcher = EventDispatcher<EventGameActivityFinished>()
    }
}