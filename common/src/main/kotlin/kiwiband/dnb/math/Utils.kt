package kiwiband.dnb.math

import kotlin.math.max
import kotlin.math.min

object MyMath {
    fun clamp(value: Int, min: Int, max: Int) = min(max(value, min), max)
}