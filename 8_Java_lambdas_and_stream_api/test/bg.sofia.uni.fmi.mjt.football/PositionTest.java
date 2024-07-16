package bg.sofia.uni.fmi.mjt.football;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionTest {

    @Test
    void testPosition() {
        assertEquals(Position.ST, Position.valueOf("ST"));
        assertEquals(Position.GK, Position.valueOf("GK"));
    }
}