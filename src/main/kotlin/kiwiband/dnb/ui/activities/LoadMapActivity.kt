package kiwiband.dnb.ui.activities

import kiwiband.dnb.events.EventKeyPress
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.map.MapSaver
import kiwiband.dnb.ui.Renderer
import kiwiband.dnb.ui.views.View

class LoadMapActivity(rootView: View, renderer: Renderer, private val finishCallback: (LocalMap) -> Unit):
    ResultActivity<LocalMap>(rootView, renderer) {

    private val mapSaver = MapSaver()
    private val mapFile = "./maps/saved_map.dnb"

    override fun onStart() {
        if (!mapSaver.checkSaved(mapFile)) {
            finish(LocalMap.generateMap(88, 32))
        }
        drawScene()
    }

    override fun onFinish(result: LocalMap?) {
        result?.let(finishCallback)
    }

    override fun onKeyPress(keyPress: EventKeyPress) {
        when (keyPress.key.character) {
            'y', 'н' -> finish(mapSaver.loadFromFile(mapFile))
            'n', 'т' -> finish(LocalMap.generateMap(88, 32))
        }
    }
}