package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GemFactoryTest {

    @Test
    void fromCsv_shouldCreatePreciousGem() {
        Gem gem = GemFactory.fromCsv("Precious;Diamond;1.5;12000;95");

        assertNotNull(gem);
        assertEquals("Diamond", gem.getName());
        assertEquals(1.5, gem.getWeightCarats());
        assertEquals(95, gem.getTransparency());
        assertEquals(12000, gem.getPricePerCarat());
        assertTrue(gem instanceof PreciousStone);
    }

    @Test
    void fromCsv_shouldThrowOnInvalidType() {
        assertThrows(IllegalArgumentException.class,
                () -> GemFactory.fromCsv("Unknown;Stone;1.0;1000;50"));
    }

    @Test
    void fromCsv_shouldThrowOnMalformedLine() {
        assertThrows(IllegalArgumentException.class,
                () -> GemFactory.fromCsv("Precious;OnlyThreeParts;12;"));
    }
}
