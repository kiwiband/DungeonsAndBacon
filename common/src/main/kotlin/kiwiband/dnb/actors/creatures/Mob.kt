package kiwiband.dnb.actors.creatures

import com.googlecode.lanterna.TextColor
import kiwiband.dnb.ASCIIART
import kiwiband.dnb.actors.ViewAppearance
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

    private val appearance: ViewAppearance

    init {
        intelligence = when (aiID) {
            0 -> AITimid(this, VIEW_RANGE).also { appearance = ViewAppearance('%') }
            1 -> AIPassive(this).also { appearance = ViewAppearance('$') }
            else -> AIAggressive(this, VIEW_RANGE).also { appearance = ViewAppearance('&', ASCIIART.RED) }
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
            game?.eventBus?.run(EventSpawnActor(DropBag(pos, ItemFactory.getRandomItem())))
        }
    }

    override fun getType() = TYPE_ID

    override fun toJSON(): JSONObject = super.toJSON().put("ai", aiID)


    companion object {
        const val TYPE_ID = "mb"
        private const val VIEW_RANGE = 5

        fun fromJSON(obj: JSONObject, map: LocalMap): Mob {
            return Mob(
                map,
                Vec2M(obj.getInt("x"), obj.getInt("y")),
                obj.getInt("ai"),
                CreatureStatus(
                    obj.getInt("lvl"),
                    obj.getInt("hp"),
                    obj.getInt("exp")
                )
            )
        }
    }
}