package kiwiband.dnb.actors.creatures

import kiwiband.dnb.actors.ViewAppearance
import kiwiband.dnb.actors.creatures.ai.AIBase
import kiwiband.dnb.actors.creatures.ai.ConfusedAIDecorator
import kiwiband.dnb.actors.creatures.status.CreatureStatus
import kiwiband.dnb.actors.statics.DropBag
import kiwiband.dnb.events.EventSpawnActor
import kiwiband.dnb.inventory.ItemFactory
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.math.Vec2M
import org.json.JSONObject
import kotlin.random.Random


class Mob constructor(
    map: LocalMap,
    position: Vec2,
    var intelligence: AIBase,
    var appearance: ViewAppearance,
    status: CreatureStatus
) : Creature(map, status) {

    init {
        pos.set(position)
    }

    override var viewPriority: Int = 100

    override fun getViewAppearance() = appearance


    override fun onTick() {
        super.onTick()
        intelligence.nextMove(this)
    }

    fun confuse() {
        intelligence = ConfusedAIDecorator(intelligence)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Random.nextInt(0, 10) == 0) {
            game?.eventBus?.run(EventSpawnActor(DropBag(pos, ItemFactory.getRandomItem())))
        }
    }

    override fun getType() = TYPE_ID

    override fun toJSON(): JSONObject = super.toJSON().put("ai", intelligence.getID())


    companion object {
        const val TYPE_ID = "mb"

        fun fromJSON(obj: JSONObject, map: LocalMap): Mob {
            return MobFactory.createMob(
                map,
                Vec2M(obj.getInt("x"), obj.getInt("y")),
                CreatureStatus(
                    obj.getInt("lvl"),
                    obj.getInt("hp"),
                    obj.getInt("exp")
                ),
                obj.getInt("ai")
            )
        }
    }
}