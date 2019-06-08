package kiwiband.dnb.ui.activities

import kiwiband.dnb.Game
import kiwiband.dnb.ServerCommunicationManager
import kiwiband.dnb.events.EventUpdateMap
import kiwiband.dnb.events.Registration
import kiwiband.dnb.ui.App
import kiwiband.dnb.ui.AppContext
import kiwiband.dnb.ui.views.LoadMapView
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.BoxLayout


/**
 * An activity that loads the map.
 * On receiving the map, finishes and runs the EventMapLoaded event.
 */
class LoadMapActivity(context: AppContext,
                      private val comm: ServerCommunicationManager,
                      callback: (Game) -> Unit):
    Activity<Game>(context, callback) {

    private lateinit var registration: Registration

    override fun createRootView(): View {
        return BoxLayout(LoadMapView(App.SCREEN_WIDTH - 2, App.SCREEN_HEIGHT - 2))
    }

    override fun onStart() {
        drawScene()
        val playerId = comm.connect()
        registration = EventUpdateMap.dispatcher.addHandler {
            registration.finish()
            val game = Game(it.newMap, playerId)
            finish(game)
        }
    }
}