package kiwiband.dnb.ui.activities

import kiwiband.dnb.events.EventKeyPress
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.map.MapSaver
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

    private val mapSaver = MapSaver()
    private val mapFile = "./maps/saved_map.dnb"


    override fun createRootView(): View {
        return BoxLayout(LoadMapView(App.SCREEN_WIDTH - 2, App.SCREEN_HEIGHT - 2))
    }

    override fun onStart() {
        if (!mapSaver.checkSaved(mapFile)) {
            finish(LocalMap.generateMap(88, 32))
            return
        }
        drawScene()
        EventKeyPress.dispatcher.addHandler { onKeyPress(it) }
    }

    private fun onKeyPress(keyPress: EventKeyPress) {
        when (keyPress.key.character) {
            'y', 'н' -> finish(mapSaver.loadFromFile(mapFile))
            'n', 'т' -> finish(LocalMap.generateMap(88, 32))
        }
    }
}