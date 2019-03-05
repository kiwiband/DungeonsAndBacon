package kiwiband.dnb.ui.views

import kiwiband.dnb.ui.MapDrawUtil.writeText

class PlayerView(width: Int, height: Int) : View(width, height) {
    override fun to2DArray(): Array<CharArray> {
        val result = Array(height) { CharArray(width) { ' ' } }

        writeText(result, "L(O_o)-(===>", 7, 1)
        writeText(result, "HERONAME", 3, 4)
        writeText(result, "HP 15/15", 3, 5)
        writeText(result, "LVL 1", 3, 6)
        writeText(result, "EXP 1/10", 3, 7)
        writeText(result, "ATK 4", 3, 8)
        writeText(result, "DEF 1", 3, 9)

        return result
    }
}