package kiwiband.dnb.ui

import kiwiband.dnb.ui.activities.Activity
import java.util.*

data class AppContext(val renderer: Renderer,
                      val activities: ArrayDeque<Activity<*>>)