package kiwiband.dnb.ui.views

import kiwiband.dnb.Colors
import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.actors.ViewAppearance
import kiwiband.dnb.math.Borders
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.GameAppContext
import kiwiband.dnb.ui.Renderer

/**
 * View containing the map.
 */
class MapView(private val gameContext: GameAppContext, width: Int, height: Int) : View(width, height) {
    private val mgr = gameContext.gameManager
    private val center = Vec2(width / 2, height / 2)
    private val borders = Vec2(0, 0) to Vec2(width, height)

    override fun draw(renderer: Renderer) {
        val offsets = getOffsets(getMapBorders())
        val offset = center - findPlayerPosition().fitIn(offsets)

        val pos = Vec2M()

        mgr.getMap().actors.forEachCellIndexed(borders - offset) { x, y, cell ->
            pos.set(x, y).add(offset)
            when {
                cell.lit -> {
                    cell.actors.firstOrNull()?.also {
                        renderActor(renderer, it, pos)
                    } ?: renderer.writeCharacter(EMPTY_CELL, pos)
                }
                cell.explored -> {
                    cell.actors.find { it.litIfExplored }?.also {
                        renderActor(renderer, it, pos)
                    } ?: renderer.writeCharacter(UNLIT_CELL, pos)
                }
                else -> renderer.writeCharacter(UNEXPLORED_CELL, pos)
            }
        }
    }

    private fun renderActor(renderer: Renderer, actor: MapActor, pos: Vec2) {
        val appearance = actor.getViewAppearance()
        if (actor === gameContext.selection.selectedActor) {
            val bgColor = appearance.bgColor
            appearance.bgColor = Colors.DARK_BLUE
            renderer.writeCharacter(appearance, pos)
            appearance.bgColor = bgColor
        } else {
            renderer.writeCharacter(appearance, pos)
        }
    }

    private fun getMapBorders() = mgr.getMap().let { Vec2(0, 0) to Vec2(it.width, it.height) }

    private fun getOffsets(mapBorders: Borders): Borders = center to (mapBorders.b - center).mixMax(center + Vec2(1, 1))

    private fun findPlayerPosition(): Vec2 {
        return mgr.getPlayer().pos
    }

    companion object {
        val EMPTY_CELL = ViewAppearance('.', Colors.LIGHT_GRAY)
        val UNLIT_CELL = ViewAppearance('.', Colors.DARK_GRAY)
        val UNEXPLORED_CELL = ViewAppearance('▒', Colors.DARK_GRAY)
    }
}
