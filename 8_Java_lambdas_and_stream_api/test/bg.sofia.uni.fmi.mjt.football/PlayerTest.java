package bg.sofia.uni.fmi.mjt.football;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {

    @Test
    void testPlayerOf() {
        String line = "Cristiano;Cristiano Ronaldo;2/5/1985;36;187.0;83.0;ST;Portugal;93;93;100000000;500000;right";
        Player player = Player.of(line);

        assertEquals("Cristiano", player.name());
        assertEquals("Cristiano Ronaldo", player.fullName());
        assertEquals(LocalDate.of(1985, 2, 5), player.birthDate());
        assertEquals(36, player.age());
        assertEquals(187.0, player.heightCm());
        assertEquals(83.0, player.weightKg());
        assertEquals(List.of(Position.ST), player.positions());
        assertEquals("Portugal", player.nationality());
        assertEquals(93, player.overallRating());
        assertEquals(93, player.potential());
        assertEquals(100000000L, player.valueEuro());
        assertEquals(500000L, player.wageEuro());
        assertEquals(Foot.RIGHT, player.preferredFoot());
    }
}
