package kiwiband.dnb.ui.views

class BoxView(width: Int, height: Int) : View(width, height) {
    override fun to2DArray(): Array<CharArray> {
        val result = Array(height) { CharArray(width) { ' ' } }
        val boxRow = CharArray(width) { '#' }
        val wallRow = CharArray(width) { ' ' }
        wallRow[0] = '#'
        wallRow[width - 1] = '#'
        result[0] = boxRow
        result[height - 1] = boxRow
        for (i in 1..(height - 2))
            result[i] = wallRow
        return result
    }
}