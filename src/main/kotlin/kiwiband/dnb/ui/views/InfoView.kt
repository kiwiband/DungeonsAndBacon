package kiwiband.dnb.ui.views

import kiwiband.dnb.ui.MapDrawUtil.writeText

class InfoView(width: Int, height: Int) : View(width, height) {

    override fun to2DArray(): Array<CharArray> {
        val result = Array(height) { CharArray(width) { ' ' } }
        val bgRow = CharArray(width) { '·' }
        val boxRow = CharArray(width) { '#' }
        boxRow[0] = '·'
        boxRow[1] = '·'
        boxRow[width - 1] = '·'
        boxRow[width - 2] = '·'
        val wallRow = CharArray(width) { ' ' }
        wallRow[0] = '·'
        wallRow[1] = '·'
        wallRow[width - 1] = '·'
        wallRow[width - 2] = '·'
        wallRow[2] = '#'
        wallRow[width - 3] = '#'

        result[0] = bgRow
        result[1] = boxRow
        result[height - 1] = bgRow
        result[height - 2] = boxRow

        for (i in 2..(height - 3)) {
            result[i] = wallRow.clone()
        }

        writeText(result, "INFO HERE", 10, 4)

        return result
    }
}