package kiwiband.dnb.actors.creatures

import kiwiband.dnb.Colors
import kiwiband.dnb.Game
import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.ViewAppearance
import kiwiband.dnb.actors.creatures.status.CreatureStatus
import kiwiband.dnb.actors.statics.DropBag
import kiwiband.dnb.events.*
import kiwiband.dnb.inventory.EquipmentItem
import kiwiband.dnb.inventory.EquipmentSet
import kiwiband.dnb.inventory.Inventory
import kiwiband.dnb.inventory.ItemFactory
import kiwiband.dnb.map.LocalMap
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.math.Vec2M
import org.json.JSONObject
import kotlin.random.Random

/**
 * Player character.
 * @param map map where the character is on
 * @param position initial position on the [map]
 */
class Player(
    map: LocalMap,
    position: Vec2,
    status: CreatureStatus,
    var playerId: String
) : Creature(map, status, TickOrder.PLAYER) {
    val appearance = ViewAppearance('@', Colors.GREEN)

    var inventory: Inventory = Inventory(20).also { it.add(ItemFactory.getRandomArmor()) }
        private set

    val equipment = EquipmentSet(this)

    init {
        super.pos.set(position)
    }

    override fun getViewAppearance(): ViewAppearance = appearance

    private var eventMove: Registration? = null
    private val moveDirection: Vec2M = Vec2M()
    override var viewPriority: Int = 10000

    override fun onBeginGame(game: Game) {
        super.onBeginGame(game)
        eventMove = game.eventBus.move.addHandler { if (it.playerId == playerId) moveDirection.set(it.direction) }
        game.eventBus.useItem.addHandler {
            if (it.playerId == playerId) {
                useItem(it.itemNum)
            }
        }
    }

    override fun onTick() {
        super.onTick()
        if (!moveDirection.isZero()) {
            move(moveDirection)
            moveDirection.set(0, 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val items = inventory.items
        for (item in items) {
            if (item is EquipmentItem) {
                item.unequip()
            }
        }
        game?.eventBus?.run(EventSpawnActor(DropBag(pos, *items.toTypedArray())))
        game?.eventBus?.run(EventGameOver())
        eventMove?.finish()
    }

    override fun blockInteract(actor: MapActor) {
        super.blockInteract(actor)
        if (actor is Mob && Random.nextFloat() < 0.2) {
            actor.confuse()
        }
    }

    override fun overlapInteract(actor: MapActor) {
        super.overlapInteract(actor)
        if (actor is DropBag) {
            while (actor.hasItems() && inventory.hasSpace()) {
                inventory.add(actor.getItem())
            }
            if (!actor.hasItems()) {
                game?.eventBus?.run(EventDestroyActor(actor))
            }
        }
    }

    override fun getType() = TYPE_ID

    private fun useItem(itemNum: Int) {
        val item = inventory[itemNum]
        if (item is EquipmentItem) {
            equipment.equip(item)
        }
    }

    override fun toJSON(): JSONObject {
        return super.toJSON().put("inv", inventory.toJSON()).put("id", playerId)
    }

    companion object {
        const val TYPE_ID = "plyr"

        fun fromJSON(obj: JSONObject, map: LocalMap): Player {
            val player = Player(
                map,
                Vec2M(obj.getInt("x"), obj.getInt("y")),
                CreatureStatus(
                    obj.getInt("lvl"),
                    obj.getInt("hp"),
                    obj.getInt("exp")
                ),
                obj.getString("id")
            )
            player.inventory = Inventory.fromJSON(obj.getJSONObject("inv"), player)
            for (item in player.inventory.items) {
                if (item is EquipmentItem && item.equipped()) {
                    player.equipment.equip(item)
                }
            }
            return player
        }
    }
}
