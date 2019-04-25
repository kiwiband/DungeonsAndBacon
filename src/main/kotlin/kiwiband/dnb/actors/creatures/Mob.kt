package kiwiband.dnb.actors.creatures

import kiwiband.dnb.actors.creatures.ai.*
import kiwiband.dnb.actors.creatures.status.CreatureStatus
import kiwiband.dnb.actors.statics.DropBag
import kiwiband.dnb.events.EventSpawnActor
import kiwiband.dnb.inventory.ItemFactory
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2M
import org.json.JSONObject
import kotlin.random.Random


class Mob(
    map: LocalMap,
    position: Vec2M,
    private val aiID: Int,
    status: CreatureStatus
) : Creature(map, status) {

    constructor(map: LocalMap, position: Vec2M, status: CreatureStatus) :
            this(map, position, Random.nextInt(3), status)

    private var intelligence: AIBase
    override fun getViewAppearance() = appearance

    private val appearance: Char

    init {
        intelligence = when (aiID) {
            0 -> AITimid(this, VIEW_RANGE).also { appearance = '%' }
            1 -> AIPassive(this).also { appearance = '$' }
            else -> AIAggressive(this, VIEW_RANGE).also { appearance = '&' }
        }
        pos.set(position)
    }

    override fun onTick() {
        super.onTick()
        intelligence.nextMove()
    }

    fun confuse() {
        intelligence = ConfusedAIDecorator(intelligence).removeFinishedDecorators()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Random.nextInt(0, 10) == 0) {
            EventSpawnActor.dispatcher.run(EventSpawnActor(DropBag(pos, ItemFactory.getRandomItem())))
        }
    }

    override fun getType() = TYPE_ID

    override fun toJSON(): JSONObject = super.toJSON().put("ai", aiID)


    companion object {
        const val TYPE_ID = "mb"
        private const val VIEW_RANGE = 5
    }
}