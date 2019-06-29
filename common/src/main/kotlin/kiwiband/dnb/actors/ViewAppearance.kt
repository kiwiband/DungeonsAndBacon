package kiwiband.dnb.actors

import com.googlecode.lanterna.TextColor

class ViewAppearance(
    var char: Char,
    var color: TextColor = TextColor.ANSI.DEFAULT,
    var bgColor: TextColor = TextColor.ANSI.DEFAULT
)