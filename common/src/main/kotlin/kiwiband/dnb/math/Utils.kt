package kiwiband.dnb.math

object MyMath {
    fun clamp(value: Int, min: Int, max: Int) = Math.min(Math.max(value, min), max)
}