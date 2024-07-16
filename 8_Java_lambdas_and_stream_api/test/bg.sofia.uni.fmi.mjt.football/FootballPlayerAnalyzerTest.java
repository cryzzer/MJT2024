package bg.sofia.uni.fmi.mjt.football;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FootballPlayerAnalyzerTest {

    private FootballPlayerAnalyzer analyzer;

    @BeforeEach
    void setUp() throws Exception {
        Path filePath = Paths.get("fifa_players_clean.csv");
        String csvData = Files.readString(filePath);
        analyzer = new FootballPlayerAnalyzer(new StringReader(csvData));
    }

    @Test
    void testGetAllPlayers() {
        List<Player> players = analyzer.getAllPlayers();
        assertEquals(17699, players.size()); // 17699 players from file
    }

    @Test
    void testGetAllNationalities() {
        Set<String> nationalities = analyzer.getAllNationalities();
        assertEquals(159, nationalities.size()); // 159 different nationalities from file
    }

    @Test
    void testGetHighestPaidPlayerByNationality() {
        Player player = analyzer.getHighestPaidPlayerByNationality("Argentina");
        assertEquals("L. Messi", player.name());
    }

    @Test
    void testGetHighestPaidPlayerByNationalityThrowsException() {
        assertThrows(NoSuchElementException.class, () -> analyzer.getHighestPaidPlayerByNationality("Nonexistent Country"));
    }

    @Test
    void testGroupByPosition() {
        var groupedPlayers = analyzer.groupByPosition();
        assertEquals(15, groupedPlayers.size()); // 15 different positions from file
        assertEquals(351, groupedPlayers.get(Position.CF).size()); // 351 players with position CF in file
    }

    @Test
    void testGetTopProspectPlayerForPositionInBudget() {
        Optional<Player> player = analyzer.getTopProspectPlayerForPositionInBudget(Position.ST, 200000000L);
        assertEquals("K. Mbappé", player.get().name());
    }

    @Test
    void testGetSimilarPlayers() {
        Player targetPlayer = Player.of("Lionel;Lionel Messi;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left");
        Set<Player> similarPlayers = analyzer.getSimilarPlayers(targetPlayer);
        assertEquals(1, similarPlayers.size());
        assertEquals("L. Messi", similarPlayers.iterator().next().name());
    }

    @Test
    void testGetPlayersByFullNameKeyword() {
        Set<Player> players = analyzer.getPlayersByFullNameKeyword("Lionel Andrés Messi");
        assertEquals(1, players.size());
        assertEquals("L. Messi", players.iterator().next().name());
    }
}
