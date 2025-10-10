package service;

import model.Gem;
import model.PreciousStone;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GemManagerTest {

    private Path tempFile;
    private GemManager gemManager;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("gems_test", ".csv");
        Files.writeString(tempFile,
                "Precious;Diamond;1.5;12000;95\n" +
                "SemiPrecious;Amethyst;5.0;500;80\n");
        gemManager = new GemManager(tempFile.toString());
        gemManager.loadFromFile();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void loadFromFile_shouldLoadGemsCorrectly() {
        List<Gem> gems = gemManager.getGems();
        assertEquals(2, gems.size());
        assertTrue(gems.get(0) instanceof PreciousStone);
        assertEquals("Diamond", gems.get(0).getName());
    }

    @Test
    void saveToFile_shouldWriteValidCsv() throws IOException {
        gemManager.saveToFile();
        String content = Files.readString(tempFile);
        assertTrue(content.contains("Diamond"));
        assertTrue(content.contains("Amethyst"));
    }

    @Test
    void getGems_shouldReturnUnmodifiableListReference() {
        List<Gem> gems = gemManager.getGems();
        assertEquals(2, gems.size());
        gems.clear(); // does not affect original internal list
        assertEquals(2, gemManager.getGems().size());
    }

    @Test
    void loadFromFile_shouldHandleInvalidLinesGracefully() throws IOException {
        Files.writeString(tempFile, "Invalid;line;missing;fields");
        gemManager = new GemManager(tempFile.toString());
        assertDoesNotThrow(gemManager::loadFromFile);
    }
}
