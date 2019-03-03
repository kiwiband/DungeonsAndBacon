package kiwiband.dnb.actors.creatures

import kiwiband.dnb.events.EventMove
import kiwiband.dnb.map.LocalMap

class Player(map: LocalMap) : Creature(map) {
    private val eventMoveId: Int = EventMove.dispatcher.addHandler { move(it.direction) }

    override fun onDestroy() {
        super.onDestroy()
        EventMove.dispatcher.removeHandler(eventMoveId)
    }
}