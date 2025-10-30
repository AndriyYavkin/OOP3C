package service;

import model.Gem;
import model.PreciousStone;
import model.SemiPreciousStone;
import org.junit.jupiter.api.*;

import java.nio.file.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GemManagerTest {

    private GemManager gemManager;
    private final Path dbPath = Paths.get("test_gems.db");

    @BeforeAll
    void initDb() throws Exception {
        // create fresh test DB
        Files.deleteIfExists(dbPath);
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS Gems");
            stmt.execute("CREATE TABLE IF NOT EXISTS Gems (" +
                    "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Type TEXT, Name TEXT, WeightCarats REAL, " +
                    "PricePerCarat REAL, Transparency INTEGER, " +
                    "CertificationID TEXT, OriginCountry TEXT)");
        }
        gemManager = new GemManager();
    }

    @AfterEach
    void cleanUp() throws Exception {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM Gems");
        }
    }

    @AfterAll
    void deleteDb() throws Exception {
        Files.deleteIfExists(dbPath);
    }

    @Test
    void saveGem_shouldInsertIntoDatabase() {
        var diamond = new PreciousStone("Diamond", 1.5, 95, 12000, "GIA-123");
        gemManager.saveGem(diamond);

        gemManager.loadFromDatabase();
        List<Gem> gems = gemManager.getGems();

        assertEquals(1, gems.size());
        assertEquals("Diamond", gems.get(0).getName());
        assertTrue(gems.get(0) instanceof PreciousStone);
    }

    @Test
    void loadFromDatabase_shouldLoadMultipleGems() {
        gemManager.saveGem(new PreciousStone("Ruby", 2.0, 90, 10000, "GRS-456"));
        gemManager.saveGem(new PreciousStone("Sapphire", 1.2, 88, 8000, "GIA-789"));

        gemManager.loadFromDatabase();
        assertEquals(2, gemManager.getGems().size());
    }

    @Test
    void saveAndLoad_shouldPreserveSpecificFields() {
        gemManager.saveGem(new PreciousStone("Emerald", 1.0, 85, 9000, "AGL-111"));
        gemManager.saveGem(new SemiPreciousStone("Amethyst", 5.0, 90, 150, "Brazil"));

        gemManager.loadFromDatabase();
        List<Gem> gems = gemManager.getGems();
        assertEquals(2, gems.size());

        Gem loadedEmerald = gems.stream().filter(g -> g.getName().equals("Emerald")).findFirst().orElse(null);
        Gem loadedAmethyst = gems.stream().filter(g -> g.getName().equals("Amethyst")).findFirst().orElse(null);

        assertNotNull(loadedEmerald);
        assertTrue(loadedEmerald instanceof PreciousStone);
        assertEquals("AGL-111", ((PreciousStone) loadedEmerald).getCertificationID());

        assertNotNull(loadedAmethyst);
        assertTrue(loadedAmethyst instanceof SemiPreciousStone);
        assertEquals("Brazil", ((SemiPreciousStone) loadedAmethyst).getOriginCountry());
    }

    @Test
    void removeGemInteractive_shouldDeleteFromDatabase() {
        gemManager.saveGem(new PreciousStone("Emerald", 1.8, 85, 9500, "SSEF-222"));
        gemManager.loadFromDatabase();

        // simulate user selecting gem #1 for deletion
        var scanner = new java.util.Scanner("1\n");
        gemManager.removeGemInteractive(scanner);

        gemManager.loadFromDatabase();
        assertTrue(gemManager.getGems().isEmpty());
    }

    @Test
    void getGems_shouldReturnIndependentList() {
        gemManager.saveGem(new SemiPreciousStone("Topaz", 2.2, 80, 1500, "Madagascar"));
        gemManager.loadFromDatabase();

        List<Gem> copy = gemManager.getGems();
        copy.clear();
        assertEquals(1, gemManager.getGems().size());
    }
}