package kiwiband.dnb.ui.views

import kiwiband.dnb.Selection
import kiwiband.dnb.math.Vec2
import kiwiband.dnb.ui.Renderer

/**
 * View with game information.
 */
class InfoView(private val selection: Selection, width: Int, height: Int) : View(width, height) {
    override fun draw(renderer: Renderer) {
        if (selection.enableSelection) {
            renderer.writeText("SELECTION ON", Vec2(8, 1))
        }
        val actor = selection.selectedActor
        if (actor == null) {
            renderer.writeText("DUNGEONS", Vec2(10, 4))
            renderer.writeText("AND", Vec2(12, 5))
            renderer.writeText("BACON", Vec2(11, 6))
        } else {
            renderer.writeText("CREATURE", Vec2(3, 4))
            renderer.writeCharacter(actor.getViewAppearance(), Vec2(12, 4))
            Drawer.drawCreatureStatus(renderer, actor.status, 4, 5)

        }
    }
}
