package kiwiband.dnb

import kiwiband.dnb.actors.creatures.Creature
import kiwiband.dnb.actors.creatures.Player
import kiwiband.dnb.math.Vec2

class Selection {
    var enableSelection = false
    var selectedActor: Creature? = null
        private set

    private val selections = mutableListOf<Creature>()
    private var curSelectedInd = -1

    private var actualSelections = false

    fun updateSelection(player: Player, radius: Int) {
        actualSelections = false
        if (!enableSelection) return
        val selected = selectedActor
        if (selected != null && selected.alive && selected.lit
            && selected.pos.distanceEuler2(player.pos) < radius * radius) {
            return
        }
        updateSelections(player, radius)
        selectedActor = null
        curSelectedInd = -1
        nextSelection(player, radius)
        enableSelection = true
    }

    private fun updateSelections(player: Player, radius: Int) {
        selections.clear()
        val mapGrid = player.map.actors
        val p = player.pos
        val r = radius - 1
        val r2 = radius * radius
        for (y in -r..r) {
            for (x in -r..r) {
                val dist2 = Vec2.normEuler2(x, y)
                if (dist2 >= r2 || dist2 == 0) {
                    continue
                }
                mapGrid.getSafe(p.x + x, p.y + y)?.also { cell ->
                    if (cell.lit) {
                        cell.actors.find { it is Creature }?.also {
                            selections.add(it as Creature)
                        }
                    }
                }
            }
        }
        selections.sortBy { Vec2.normEuler2(it.pos.x - p.x, it.pos.y - p.y) }
        actualSelections = true
    }

    fun nextSelection(player: Player, radius: Int) {
        if (!actualSelections) {
            updateSelections(player, radius)
        }
        ++curSelectedInd
        if (curSelectedInd == selections.size) {
            enableSelection = if (selections.size != 0) false else !enableSelection
            curSelectedInd = -1
            selectedActor = null
        } else {
            selectedActor = selections[curSelectedInd]
            enableSelection = true
        }
    }
}