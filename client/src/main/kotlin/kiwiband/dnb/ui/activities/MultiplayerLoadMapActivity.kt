package kiwiband.dnb.ui.activities

import kiwiband.dnb.ServerCommunicationManager
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.ui.AppContext
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.BoxLayout
import kiwiband.dnb.ui.views.layout.BoxSlot
import kiwiband.dnb.ui.views.TextView
import kiwiband.dnb.ui.views.layout.util.HorizontalAlignment
import kiwiband.dnb.ui.views.layout.util.VerticalAlignment


/**
 * An activity that loads the map.
 * On receiving the map, finishes and runs the EventMapLoaded event.
 */
class MultiplayerLoadMapActivity(context: AppContext,
                                 private val comm: ServerCommunicationManager,
                                 callback: (Pair<String, LocalMap>) -> Unit):
    Activity<Pair<String, LocalMap>>(context, callback) {

    override fun createRootView(): View {
        val size = context.renderer.screen.terminalSize
        return BoxLayout(
            TextView(TEXT), BoxSlot(HorizontalAlignment.CENTER, VerticalAlignment.CENTER),
            size.columns, size.rows)
    }

    override fun onStart() {
        drawScene()
        finish(comm.connect())
    }

    companion object {
        const val TEXT = "Loading map..."
    }
}