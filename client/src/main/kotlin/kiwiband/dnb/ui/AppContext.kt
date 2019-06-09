package kiwiband.dnb.ui

import kiwiband.dnb.events.EventBus
import kiwiband.dnb.manager.GameManager
import kiwiband.dnb.ui.activities.Activity
import java.util.*

open class AppContext(
    val renderer: Renderer,
    val activities: ArrayDeque<Activity<*>>,
    val eventBus: EventBus
)

class GameAppContext(
    renderer: Renderer,
    activities: ArrayDeque<Activity<*>>,
    eventBus: EventBus,
    val gameManager: GameManager
) : AppContext(renderer, activities, eventBus) {

    constructor(context: AppContext, gameManager: GameManager, eventBus: EventBus) :
            this(context.renderer, context.activities, eventBus, gameManager)
}