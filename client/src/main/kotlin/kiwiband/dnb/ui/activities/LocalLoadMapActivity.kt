package kiwiband.dnb.ui.activities

import kiwiband.dnb.Settings
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.map.MapSaver
import kiwiband.dnb.ui.AppContext
import kiwiband.dnb.ui.views.View
import kiwiband.dnb.ui.views.layout.BoxLayout
import kiwiband.dnb.ui.views.layout.BoxSlot
import kiwiband.dnb.ui.views.layout.TextView
import kiwiband.dnb.ui.views.layout.util.HorizontalAlignment
import kiwiband.dnb.ui.views.layout.util.VerticalAlignment

class LocalLoadMapActivity(
    context: AppContext,
    private val mapSaver: MapSaver,
    private val mapFile: String,
    callback: (LocalMap) -> Unit
) : Activity<LocalMap>(context, callback) {

    var hasMap = true

    override fun createRootView(): View {
        val size = context.renderer.screen.terminalSize
        return BoxLayout(TextView(TEXT), BoxSlot(HorizontalAlignment.CENTER, VerticalAlignment.CENTER),
            size.columns, size.rows)
    }

    override fun onStart() {
        if (!mapSaver.checkSaved(mapFile)) {
            hasMap = false
            finish(createMap())
            return
        }
        drawScene()
        context.eventBus.pressKey.pushLayer()
        context.eventBus.pressKey.addHandler { event ->
            when (event.key.character) {
                'y', 'н' -> finish(mapSaver.loadFromFile(mapFile))
                'n', 'т' -> finish(createMap())
            }
        }
    }

    private fun createMap() = LocalMap.generateMap(Settings.mapWidth, Settings.mapHeight)

    override fun onFinish(result: LocalMap) {
        if (hasMap) {
            context.eventBus.pressKey.popLayer()
        }
    }

    companion object {
        const val TEXT = "Load an existing map?\n(y/n)"
    }
}