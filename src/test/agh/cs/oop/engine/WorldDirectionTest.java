package agh.cs.oop.engine;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;

public class WorldDirectionTest {
    @Test
    void nextMethod() {
        assertEquals(WorldDirection.NORTH.next(), WorldDirection.NORTH_EAST);
        assertEquals(WorldDirection.NORTH_EAST.next(), WorldDirection.EAST);
        assertEquals(WorldDirection.EAST.next(), WorldDirection.SOUTH_EAST);
        assertEquals(WorldDirection.SOUTH_EAST.next(), WorldDirection.SOUTH);
        assertEquals(WorldDirection.SOUTH.next(), WorldDirection.SOUTH_WEST);
        assertEquals(WorldDirection.SOUTH_WEST.next(), WorldDirection.WEST);
        assertEquals(WorldDirection.WEST.next(), WorldDirection.NORTH_WEST);
        assertEquals(WorldDirection.NORTH_WEST.next(), WorldDirection.NORTH);
    }
}
