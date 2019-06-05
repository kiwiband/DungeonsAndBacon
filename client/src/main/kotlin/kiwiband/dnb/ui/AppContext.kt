package kiwiband.dnb.ui

import kiwiband.dnb.manager.GameManager
import kiwiband.dnb.ui.activities.Activity
import java.util.*

open class AppContext(val renderer: Renderer,
                 val activities: ArrayDeque<Activity<*>>
)

class GameAppContext(
    renderer: Renderer,
    activities: ArrayDeque<Activity<*>>,
    val gameManager: GameManager): AppContext(renderer, activities) {

    constructor(context: AppContext, gameManager: GameManager) :
            this(context.renderer, context.activities, gameManager)
}