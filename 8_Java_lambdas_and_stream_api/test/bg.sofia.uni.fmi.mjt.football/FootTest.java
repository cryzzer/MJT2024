package bg.sofia.uni.fmi.mjt.football;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FootTest {

    @Test
    void testFootOf() {
        assertEquals(Foot.LEFT, Foot.of("left"));
        assertEquals(Foot.RIGHT, Foot.of("right"));
    }
}