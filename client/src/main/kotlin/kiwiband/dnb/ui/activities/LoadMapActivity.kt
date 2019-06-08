package kiwiband.dnb.ui.activities

import kiwiband.dnb.events.EventUpdateMap
import kiwiband.dnb.events.Registration
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.ui.App
import kiwiband.dnb.ui.AppContext
import kiwiband.dnb.ui.views.LoadMapView
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.BoxLayout


/**
 * An activity that loads the map.
 * On receiving the map, finishes and runs the EventMapLoaded event.
 */
class LoadMapActivity(context: AppContext, callback: (LocalMap) -> Unit):
    Activity<LocalMap>(context, callback) {

    private lateinit var registration: Registration

    override fun createRootView(): View {
        return BoxLayout(LoadMapView(App.SCREEN_WIDTH - 2, App.SCREEN_HEIGHT - 2))
    }

    override fun onStart() {
        registration = EventUpdateMap.dispatcher.addHandler {
            registration.finish()
            finish(it.newMap)
        }
    }
}