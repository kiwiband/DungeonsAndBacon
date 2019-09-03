package kiwiband.dnb.ui.activities

import kiwiband.dnb.ServerCommunicationManager
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.ui.AppContext
import kiwiband.dnb.ui.views.TextView
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.BoxLayout
import kiwiband.dnb.ui.views.layout.BoxNode
import kiwiband.dnb.ui.views.layout.util.Alignment.CENTER


/**
 * An activity that loads the map.
 * On receiving the map, finishes and runs the EventMapLoaded event.
 */
class MultiplayerLoadMapActivity(context: AppContext,
                                 private val comm: ServerCommunicationManager,
                                 callback: (Pair<String, LocalMap>) -> Unit):
    Activity<Pair<String, LocalMap>>(context, callback) {

    override fun createRootView(): View {
        val size = context.renderer.screenSize
        return BoxLayout(TextView(TEXT), BoxNode(CENTER), size.x, size.y)
    }

    override fun afterStart() {
        finish(comm.connect())
    }

    companion object {
        const val TEXT = "Loading map..."
    }
}