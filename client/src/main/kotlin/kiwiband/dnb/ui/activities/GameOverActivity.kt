package kiwiband.dnb.ui.activities

import kiwiband.dnb.ui.App
import kiwiband.dnb.ui.GameAppContext
import kiwiband.dnb.ui.views.GameOverView
import kiwiband.dnb.ui.views.View

class GameOverActivity(gameContext: GameAppContext):
    Activity<Unit>(gameContext, {}) {
    override fun createRootView(): View {
        return GameOverView(App.SCREEN_WIDTH, App.SCREEN_HEIGHT)
    }

    override fun onStart() {
        drawScene()
    }
}