package kiwiband.dnb.math

import kiwiband.dnb.math.Collision.*
import org.junit.Assert.assertEquals
import org.junit.Test

class CollisionTest {
    @Test
    fun testCollide() {
        assertEquals(Block, Block.collide(Block))
        assertEquals(Ignore, Block.collide(Ignore))
        assertEquals(Overlap, Block.collide(Overlap))

        assertEquals(Ignore, Ignore.collide(Block))
        assertEquals(Ignore, Ignore.collide(Ignore))
        assertEquals(Ignore, Ignore.collide(Overlap))

        assertEquals(Overlap, Overlap.collide(Block))
        assertEquals(Overlap, Overlap.collide(Ignore))
        assertEquals(Overlap, Overlap.collide(Overlap))
    }
}