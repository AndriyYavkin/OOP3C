package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NecklaceTest {

    private Necklace necklace;

    @BeforeEach
    void setUp() {
        necklace = new Necklace("Test Necklace");
        necklace.addGem(new PreciousStone("Diamond", 1.5, 95, 12000));
        necklace.addGem(new SemiPreciousStone("Amethyst", 9.2, 80, 500));
        necklace.addGem(new PreciousStone("Emerald", 2.0, 90, 8000));
        necklace.addGem(new SemiPreciousStone("Opal", 2.5, 70, 900));
    }

    @Test
    void totalWeight_shouldSumAllCarats() {
        double expected = 1.5 + 9.2 + 2.0 + 2.5;
        assertEquals(expected, necklace.getTotalWeight(), 1e-6);
    }

    @Test
    void totalPrice_shouldBePositiveAndReasonable() {
        double total = necklace.getTotalPrice();
        assertTrue(total > 0);
        assertEquals(
                1.5 * 12000 + 9.2 * 500 + 2.0 * 8000 + 2.5 * 900,
                total,
                1e-6
        );
    }

    @Test
    void sortByTotalPriceDesc_shouldReturnDescendingOrder() {
        List<Gem> sorted = necklace.sortByTotalPriceDesc();
        assertEquals("Diamond", sorted.get(0).getName());
        assertTrue(sorted.get(0).calculatePrice() >= sorted.get(1).calculatePrice());
    }

    @Test
    void findByTransparencyRange_shouldFilterCorrectly() {
        List<Gem> filtered = necklace.findByTransparencyRange(80, 95);
        assertTrue(filtered.stream()
                .allMatch(g -> g.getTransparency() >= 80 && g.getTransparency() <= 95));
    }

    @Test
    void findByTransparencyRange_shouldThrowOnInvalidRange() {
        assertThrows(IllegalArgumentException.class,
                () -> necklace.findByTransparencyRange(120, 10));
    }
}
