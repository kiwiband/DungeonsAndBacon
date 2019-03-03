package kiwiband.dnb.math

enum class Collision {
    Block, Ignore, Overlap;

    /**
     * Default collision manager with logic:
     *  B + B = B
     *  B + I = I
     *  B + O = O
     *  I + _ = I
     *  O + _ = O
     */
    fun collide(c: Collision): Collision {
        return if (this == Block) c else this
    }
}