package kiwiband.dnb.ui.activities

import kiwiband.dnb.ui.GameAppContext
import kiwiband.dnb.ui.views.TextView
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.WrapperLayout
import kiwiband.dnb.ui.views.layout.WrapperNode
import kiwiband.dnb.ui.views.layout.util.Alignment.CENTER

class GameOverActivity(val gameContext: GameAppContext):
    Activity<Unit>(gameContext, {}) {

    override fun createRootView(): View {
        val size = context.renderer.screenSize
        return WrapperLayout(
            TextView(TEXT), WrapperNode(CENTER), size.x, size.y
        )
    }

    override fun onStart() {
        drawScene()
        context.eventBus.pressKey.addHandler {
            gameContext.gameManager.finishGame()
            finish(Unit)
        }
    }

    companion object {
        const val TEXT = "GAME OVER :(\nPRESS ANY KEY TO EXIT"
    }
}