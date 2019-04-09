package kiwiband.dnb.math

import org.junit.Assert
import org.junit.Test

class Vec2Test {
    @Test
    fun fitInTest() {
        val borders = Vec2(0, 1) to Vec2(3, 4)
        Assert.assertEquals(Vec2(2, 3), Vec2(5, 10).fitIn(borders))
        Assert.assertEquals(Vec2(1, 2), Vec2(1, 2).fitIn(borders))
        Assert.assertEquals(Vec2(0, 1), Vec2(-12, -1).fitIn(borders))
        Assert.assertEquals(Vec2(1, 3), Vec2(1, 6).fitIn(borders))
        Assert.assertEquals(Vec2(0, 2), Vec2(-3, 2).fitIn(borders))
    }

    @Test
    fun fitInIncludedTest() {
        val borders = Vec2(0, 1) to Vec2(3, 4)
        Assert.assertEquals(Vec2(3, 4), Vec2(5, 10).fitInIncluded(borders))
        Assert.assertEquals(Vec2(1, 2), Vec2(1, 2).fitInIncluded(borders))
        Assert.assertEquals(Vec2(0, 1), Vec2(-12, -1).fitInIncluded(borders))
        Assert.assertEquals(Vec2(1, 4), Vec2(1, 6).fitInIncluded(borders))
        Assert.assertEquals(Vec2(0, 2), Vec2(-3, 2).fitInIncluded(borders))
    }
}