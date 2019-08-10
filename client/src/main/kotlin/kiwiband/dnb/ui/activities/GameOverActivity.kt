package kiwiband.dnb.ui.activities

import kiwiband.dnb.ui.GameAppContext
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.TextView
import kiwiband.dnb.ui.views.layout.WrapperLayout
import kiwiband.dnb.ui.views.layout.WrapperSlot
import kiwiband.dnb.ui.views.layout.util.HorizontalAlignment
import kiwiband.dnb.ui.views.layout.util.VerticalAlignment

class GameOverActivity(gameContext: GameAppContext):
    Activity<Unit>(gameContext, {}) {

    override fun createRootView(): View {
        val size = context.renderer.screen.terminalSize
        return WrapperLayout(
            TextView(TEXT), WrapperSlot(HorizontalAlignment.CENTER, VerticalAlignment.CENTER),
            size.columns, size.rows)
    }

    override fun onStart() {
        drawScene()
    }

    companion object {
        const val TEXT = "GAME OVER :(\nPRESS ANY KEY TO EXIT"
    }
}