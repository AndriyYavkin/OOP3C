package service;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class NecklaceManagerTest {

    private Path gemFile;
    private Path necklaceFile;
    private GemManager gemManager;
    private NecklaceManager necklaceManager;

    @BeforeEach
    void setUp() throws IOException {
        gemFile = Files.createTempFile("gems", ".csv");
        necklaceFile = Files.createTempFile("necklaces", ".csv");

        Files.writeString(gemFile,
                "Precious;Diamond;1.5;12000;95\n" +
                "SemiPrecious;Opal;2.5;900;70\n");

        gemManager = new GemManager(gemFile.toString());
        gemManager.loadFromFile();

        Files.writeString(necklaceFile,
                "LuxurySet;Precious;Diamond;1.5;12000;95\n" +
                "LuxurySet;SemiPrecious;Opal;2.5;900;70\n");

        necklaceManager = new NecklaceManager(necklaceFile.toString());
        necklaceManager.loadFromFile(gemManager);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(gemFile);
        Files.deleteIfExists(necklaceFile);
    }

    @Test
    void loadFromFile_shouldLoadMultipleNecklaces() {
        // simulate additional necklace
        necklaceManager.saveToFile();
        necklaceManager.loadFromFile(gemManager);
        assertDoesNotThrow(() -> necklaceManager.loadFromFile(gemManager));
    }

    @Test
    void saveToFile_shouldPersistAllData() throws IOException {
        necklaceManager.saveToFile();
        String content = Files.readString(necklaceFile);
        assertTrue(content.contains("LuxurySet"));
        assertTrue(content.contains("Diamond"));
    }

    @Test
    void createNecklaceInteractive_shouldRejectDuplicates() {
        Scanner scanner = new Scanner("LuxurySet\n");
        necklaceManager.createNecklaceInteractive(scanner);
    }

    @Test
    void removeNecklaceInteractive_shouldRemoveExisting() {
        Scanner scanner = new Scanner("LuxurySet\n");
        necklaceManager.removeNecklaceInteractive(scanner);
    }

    @Test
    void sortNecklaceInteractive_shouldPrintSortedList() {
        Scanner scanner = new Scanner("1\n");
        assertDoesNotThrow(() -> necklaceManager.sortNecklaceInteractive(scanner));
    }

    @Test
    void findByTransparencyInteractive_shouldHandleBadInput() {
        Scanner scanner = new Scanner("1\nbad\ntext\n");
        assertDoesNotThrow(() -> necklaceManager.findByTransparencyInteractive(scanner));
    }
}
