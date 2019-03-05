package kiwiband.dnb.ui

import kiwiband.dnb.InputManager
import kiwiband.dnb.map.LocalMap

abstract class App(private val map: LocalMap, private val inputManager: InputManager) {
    abstract fun drawScene()
}