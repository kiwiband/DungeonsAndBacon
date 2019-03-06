package kiwiband.dnb.ui.views

import com.googlecode.lanterna.screen.Screen
import kiwiband.dnb.math.Vec2M
import kiwiband.dnb.ui.MapDrawUtil
import kiwiband.dnb.ui.MapDrawUtil.writeText

class PlayerView(width: Int, height: Int) : View(width, height) {
    override fun draw(screen: Screen, offset: Vec2M) {
        writeText(screen, "L(O_o)-(===>", offset + Vec2M(7, 1))

        writeText(screen, "HERONAME", offset + Vec2M(3, 4))
        writeText(screen, "HP 15/15",offset + Vec2M(3, 5))
        writeText(screen, "LVL 1",offset + Vec2M(3, 6))
        writeText(screen, "EXP 1/10",offset + Vec2M(3, 7))
        writeText(screen, "ATK 4",offset + Vec2M(3, 8))
        writeText(screen, "DEF 1",offset + Vec2M(3, 9))
    }
}