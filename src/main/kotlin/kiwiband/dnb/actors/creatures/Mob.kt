package kiwiband.dnb.actors.creatures

import kiwiband.dnb.actors.creatures.ai.AIAggressive
import kiwiband.dnb.actors.creatures.ai.AIBase
import kiwiband.dnb.actors.creatures.ai.AIPassive
import kiwiband.dnb.actors.creatures.ai.AITimid
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2M
import org.json.JSONObject
import kotlin.random.Random


class Mob(map: LocalMap, position: Vec2M, private val aiID: Int) : Creature(map) {
    constructor(map: LocalMap, position: Vec2M) : this(map, position, Random.nextInt(3))

    private val intelligence: AIBase
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

    override fun getType() = TYPE_ID

    override fun toJSON(): JSONObject = super.toJSON().put("ai", aiID)


    companion object {
        const val TYPE_ID = "mb"
        private const val VIEW_RANGE = 5
    }
}