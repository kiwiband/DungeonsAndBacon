package kiwiband.dnb.ui.views

import com.googlecode.lanterna.screen.Screen
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.MapDrawUtil.writeText

class InfoView(width: Int, height: Int) : View(width, height) {
    override fun draw(screen: Screen, offset: Vec2M) {
        writeText(screen, "DUNGEONS", offset + Vec2M(10, 4))
        writeText(screen, "AND", offset + Vec2M(12, 5))
        writeText(screen, "BACONS", offset + Vec2M(11, 6))
    }
}