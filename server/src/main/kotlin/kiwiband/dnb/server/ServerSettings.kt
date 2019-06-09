package kiwiband.dnb.server

import kiwiband.dnb.Settings

object ServerSettings {
    var port = Settings.Default.PORT
    var tickTime: Long = Default.TICK_TIME

    object Default {
        const val TICK_TIME: Long = 1000
    }
}