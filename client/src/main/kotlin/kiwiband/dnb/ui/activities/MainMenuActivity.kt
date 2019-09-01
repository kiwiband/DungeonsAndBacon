package kiwiband.dnb.ui.activities

import com.googlecode.lanterna.input.KeyType
import kiwiband.dnb.ClientSettings
import kiwiband.dnb.Game
import kiwiband.dnb.ServerCommunicationManager
import kiwiband.dnb.Settings
import kiwiband.dnb.events.EventCloseGame
import kiwiband.dnb.events.EventPressKey
import kiwiband.dnb.manager.GameManager
import kiwiband.dnb.manager.LocalGameManager
import kiwiband.dnb.manager.MultiplayerGameManager
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.map.MapSaver
import kiwiband.dnb.ui.AppContext
import kiwiband.dnb.ui.GameAppContext
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.TextView
import kiwiband.dnb.ui.views.layout.*
import kiwiband.dnb.ui.views.layout.util.HorizontalAlignment
import kiwiband.dnb.ui.views.layout.util.Padding
import java.util.concurrent.locks.ReentrantLock

class MainMenuActivity(context: AppContext, private val eventLock: ReentrantLock) : Activity<Unit>(context, {}) {
    private lateinit var menu: InteractiveSequenceLayout<VerticalSlot, MenuButton>

    override fun createRootView(): View {
        val size = context.renderer.screen.terminalSize
        val width = size.columns
        val height = size.rows
        return VerticalLayout(width, height).also { root ->
            root.addChild(
                TextView("⚑ DUNGEONS AND BACON ⚑"),
                VerticalSlot(Padding(top = 2), alignment = HorizontalAlignment.CENTER)
            )
            root.addChild(
                TextView("__________________"),
                VerticalSlot(Padding(bottom = 1), alignment = HorizontalAlignment.CENTER)
            )
            menu = object : InteractiveVerticalLayout<MenuButton>(
                width,
                height,
                LastElementBehavior.STOP
            ) {
                override fun createChild(view: View, slot: VerticalSlot): MenuButton {
                    throw NotImplementedError()
                }
            }
            root.addChild(menu)
        }
    }

    override fun onAppear() {
        val mapSaver = MapSaver()
        val mapFile = ClientSettings.mapFile
        menu.clear()
        if (mapSaver.checkSaved(mapFile)) {
            menu.addChild(MenuButton("Continue") {
                startSingleplayerGame(mapSaver.loadFromFile(mapFile), mapSaver, mapFile)
            })
        }
        menu.addChild(MenuButton("New Game") {
            startSingleplayerGame(createMap(), mapSaver, mapFile)
        })
        menu.addChild(MenuButton("Connect", this::startMultiplayerGame))
        menu.addChild(MenuButton("Exit") { exit() })
    }

    private fun createMap() = LocalMap.generateMap(Settings.mapWidth, Settings.mapHeight)

    private fun startSingleplayerGame(map: LocalMap, mapSaver: MapSaver, mapFile: String) {
        startGame(LocalGameManager(Game(map, context.eventBus), mapSaver, mapFile)) {}
    }

    private fun startMultiplayerGame() {
        val commManager = ServerCommunicationManager(
            Settings.host, Settings.port, eventLock, context.eventBus,
            ClientSettings.playerId, ClientSettings.sessionId
        )
        MultiplayerLoadMapActivity(context, commManager) { (playerId, map) ->
            startGame(MultiplayerGameManager(commManager, playerId, map, context.eventBus)) {
                commManager.disconnect()
            }
        }.start()
    }

    private fun startGame(mgr: GameManager, gameOver: () -> Unit) {
        val handler = context.eventBus.closeGame.addHandler {
            mgr.finishGame()
        }
        val gameContext = GameAppContext(context, mgr, context.eventBus)

        GameActivity(gameContext) { gameResult ->
            gameOver.invoke()
            if (gameResult) {
                GameOverActivity(gameContext).also {
                    it.start()
                    it.drawScene()
                }
            }
            handler.finish()
        }.start()
    }

    private fun onKeyPressed(pressKey: EventPressKey) {
        if (pressKey.key.keyType == KeyType.Escape) {
            exit()
            return
        }
        if (pressKey.key.keyType == KeyType.Enter) {
            menu.interact()
            return
        }
        when (pressKey.key.character) {
            'w', 'ц' -> {
                menu.previous()
                drawScene()
            }
            's', 'ы' -> {
                menu.next()
                drawScene()
            }
        }
    }

    private fun exit() {
        finish(Unit)
        context.eventBus.run(EventCloseGame())
    }

    override fun onStart() {
        context.eventBus.pressKey.addHandler { onKeyPressed(it) }
    }

    private inner class MenuButton(title: String, val interract: () -> Unit): BoxedChildView<VerticalSlot>(
        WrapperLayout(TextView(title), WrapperSlot(HorizontalAlignment.CENTER), 30, 1),
        VerticalSlot(alignment = HorizontalAlignment.CENTER)
    ) {
        override fun onInteract() {
            interract()
        }
    }
}