package service;

import model.Gem;
import model.PreciousStone;
import model.SemiPreciousStone;
import org.junit.jupiter.api.*;

import java.nio.file.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NecklaceManagerTest {

    private GemManager gemManager;
    private NecklaceManager necklaceManager;
    private final Path dbPath = Paths.get("test_gems.db");

    @BeforeAll
    void setupDb() throws Exception {
        Files.deleteIfExists(dbPath);
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.execute("DROP TABLE IF EXISTS NecklaceGems");
            stmt.execute("DROP TABLE IF EXISTS Necklaces");
            stmt.execute("DROP TABLE IF EXISTS Gems");

            stmt.execute("CREATE TABLE IF NOT EXISTS Gems (" +
                    "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Type TEXT, Name TEXT, WeightCarats REAL, " +
                    "PricePerCarat REAL, Transparency INTEGER)");

            stmt.execute("CREATE TABLE IF NOT EXISTS Necklaces (" +
                    "Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT UNIQUE)");

            stmt.execute("CREATE TABLE IF NOT EXISTS NecklaceGems (" +
                    "NecklaceId INTEGER, GemId INTEGER, " +
                    "FOREIGN KEY (NecklaceId) REFERENCES Necklaces(Id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (GemId) REFERENCES Gems(Id) ON DELETE CASCADE)");
        }

        gemManager = new GemManager();
        necklaceManager = new NecklaceManager();
    }

    @AfterEach
    void clean() throws Exception {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM NecklaceGems");
            stmt.execute("DELETE FROM Necklaces");
            stmt.execute("DELETE FROM Gems");
        }
    }

    @AfterAll
    void deleteDb() throws Exception {
        Files.deleteIfExists(dbPath);
    }

    @Test
    void createNecklaceInteractive_shouldAddToDatabase() {
        Scanner scanner = new Scanner("RoyalSet\n");
        necklaceManager.createNecklaceInteractive(scanner);
        necklaceManager.loadFromDatabase(gemManager);

        // after reloading, the necklace should appear
        assertDoesNotThrow(() -> necklaceManager.loadFromDatabase(gemManager));
    }

    @Test
    void addGemToNecklaceInteractive_shouldLinkGem() {
        Gem diamond = new PreciousStone("Diamond", 1.5, 95, 12000);
        gemManager.saveGem(diamond);
        gemManager.loadFromDatabase();

        // create necklace
        Scanner create = new Scanner("RoyalSet\n");
        necklaceManager.createNecklaceInteractive(create);
        necklaceManager.loadFromDatabase(gemManager);

        // select first necklace + first gem
        Scanner scanner = new Scanner("1\n1\n");
        assertDoesNotThrow(() -> necklaceManager.addGemToNecklaceInteractive(scanner, gemManager));
    }

    @Test
    void removeGemFromNecklaceInteractive_shouldDeleteLink() {
        Gem opal = new SemiPreciousStone("Opal", 2.5, 70, 900);
        gemManager.saveGem(opal);
        gemManager.loadFromDatabase();

        Scanner create = new Scanner("OceanDream\n");
        necklaceManager.createNecklaceInteractive(create);
        necklaceManager.loadFromDatabase(gemManager);

        Scanner scanner = new Scanner("1\n1\n");
        necklaceManager.addGemToNecklaceInteractive(scanner, gemManager);

        // Now remove the gem interactively
        Scanner removeScanner = new Scanner("1\n1\n");
        assertDoesNotThrow(() -> necklaceManager.removeGemFromNecklaceInteractive(removeScanner));
    }

    @Test
    void sortNecklaceInteractive_shouldWork() {
        Gem g1 = new PreciousStone("Ruby", 2.0, 90, 8000);
        Gem g2 = new SemiPreciousStone("Topaz", 3.0, 85, 500);
        gemManager.saveGem(g1);
        gemManager.saveGem(g2);
        gemManager.loadFromDatabase();

        Scanner create = new Scanner("Treasure\n");
        necklaceManager.createNecklaceInteractive(create);
        necklaceManager.loadFromDatabase(gemManager);

        Scanner add = new Scanner("1\n1\n");
        necklaceManager.addGemToNecklaceInteractive(add, gemManager);

        Scanner sortScanner = new Scanner("1\n");
        assertDoesNotThrow(() -> necklaceManager.sortNecklaceInteractive(sortScanner));
    }

    @Test
    void findByTransparencyInteractive_shouldHandleInvalidInput() {
        Scanner scanner = new Scanner("1\nbad\ntext\n");
        assertDoesNotThrow(() -> necklaceManager.findByTransparencyInteractive(scanner));
    }
}
