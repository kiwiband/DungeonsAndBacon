package kiwiband.dnb.server

import kiwiband.dnb.Game
import kiwiband.dnb.map.LocalMap

class GameSession(map: LocalMap) {
    val game: Game = Game(map);
}