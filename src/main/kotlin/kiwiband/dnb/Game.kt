package kiwiband.dnb

import kiwiband.dnb.events.EventTick
import kiwiband.dnb.map.GlobalMap

class Game {
    var tickTime = 0
        private set
    var globalMap: GlobalMap = GlobalMap()

    init {
        // think about delete handler from dispatcher
        EventTick.dispatcher.addHandler { onTick() }
    }

    private fun onTick() {
        tickTime++
    }
}
