package kiwiband.dnb.map

import kiwiband.dnb.actors.MapActor
import kiwiband.dnb.math.Vec2
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.UnsupportedOperationException

class MapGridTest {
    @Test
    fun testUpdateOne() {
        val grid = MapGrid(2, 2)
        val actor = MockActor()

        grid.add(actor)
        assertEquals(1, grid[0, 0].size)
        assertEquals(0, grid[1, 1].size)

        actor.pos.set(1, 1)
        assertEquals(1, grid[0, 0].size)
        assertEquals(0, grid[1, 1].size)

        grid.updateOne(Vec2(0, 0))
        assertEquals(0, grid[0, 0].size)
        assertEquals(1, grid[1, 1].size)
    }

    @Test
    fun testUpdateWithManyActors() {
        val grid = MapGrid(2, 2)
        val actor1 = MockActor()
        val actor2 = MockActor()
        val actor3 = MockActor()

        grid.add(actor1)
        grid.add(actor2)
        grid.add(actor3)

        assertEquals(3, grid[0, 0].size)
        assertEquals(0, grid[1, 1].size)

        actor1.pos.set(1, 1)
        assertEquals(3, grid[0, 0].size)
        assertEquals(0, grid[1, 1].size)

        grid.updateOne(Vec2(0, 0))
        assertEquals(2, grid[0, 0].size)
        assertEquals(1, grid[1, 1].size)

        actor2.pos.set(1, 1)
        actor3.pos.set(1, 1)
        grid.updateOne(Vec2(0, 0))
        assertEquals(1, grid[0, 0].size)
        assertEquals(2, grid[1, 1].size)
    }

    @Test
    fun testIterator() {
        val grid = MapGrid(2, 2)
        val actorList = List(5) { MockActor() }
        actorList[0].pos.set(0, 0)
        actorList[1].pos.set(0, 0)
        actorList[2].pos.set(1, 0)
        actorList[3].pos.set(1, 1)
        actorList[4].pos.set(1, 1)

        actorList.forEach { assertEquals(0, it.viewPriority) }
        actorList.forEach { grid.add(it) }

        var ind = 1
        for (ac in grid) {
            ac.viewPriority = ind++
        }

        var sum = 0
        actorList.forEach { sum += it.viewPriority }
        assertEquals(15, sum)
    }
}

class MockActor : MapActor() {
    override fun getViewAppearance(): Char = throw UnsupportedOperationException()
}