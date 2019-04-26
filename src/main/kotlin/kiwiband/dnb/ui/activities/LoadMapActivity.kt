package kiwiband.dnb.ui.activities

import kiwiband.dnb.events.EventActivityFinished
import kiwiband.dnb.events.EventDispatcher
import kiwiband.dnb.events.EventKeyPress
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.map.MapSaver
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View

class LoadMapActivity(rootView: View, renderer: Renderer):
    ResultActivity<LocalMap>(rootView, renderer) {

    private val mapSaver = MapSaver()
    private val mapFile = "./maps/saved_map.dnb"

    override fun onStart() {
        if (!mapSaver.checkSaved(mapFile)) {
            finish(LocalMap.generateMap(88, 32))
        }
        drawScene()
        EventKeyPress.dispatcher.addHandler { onKeyPress(it) }
    }

    override fun onFinish(result: LocalMap) {
        EventMapLoaded.dispatcher.run(EventMapLoaded(result))
    }

    private fun onKeyPress(keyPress: EventKeyPress) {
        when (keyPress.key.character) {
            'y', 'н' -> finish(mapSaver.loadFromFile(mapFile))
            'n', 'т' -> finish(LocalMap.generateMap(88, 32))
        }
    }
}

class EventMapLoaded(result: LocalMap): EventActivityFinished<LocalMap>(result) {
    companion object {
        val dispatcher = EventDispatcher<EventMapLoaded>()
    }
}