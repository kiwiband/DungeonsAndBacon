package kiwiband.dnb

import com.googlecode.lanterna.TextColor

object ASCIIART {
    val SHIELD = """
        |.-.-.
        || . |
        |\___/
    """.trimMargin()

    val ARMOR = """
        | _͜_
        |( ҉ )
        | \_/
    """.trimMargin()

    val BRASSIERE = """
        |
        |└⚪-⚪┘
        |
    """.trimMargin()

    val SWORD = """
        |  |
        |  ║
        | `T`
    """.trimMargin()

    val AXE = """
        | ┌─┐
        | ║_/
        | ║
    """.trimMargin()

    val BAD_ICON = """
        |
        | @_@
        |
    """.trimMargin()

    const val PLAYER = "L(O_o)-(===>"

    val GREEN = TextColor.RGB(0, 255, 0)
    val RED = TextColor.RGB(255, 0, 0)
    val WHITE = TextColor.RGB(255, 255, 255)
    val DARK_GRAY = TextColor.RGB(60, 60, 60)
    val LIGHT_GRAY = TextColor.RGB(140, 140, 140)
}