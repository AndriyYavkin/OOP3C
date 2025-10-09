package service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import model.Gem;
import model.GemFactory;
import model.Necklace;

/**
 * Manages loading and operations on a Necklace.
 */
public class NecklaceManager {

    private final Necklace necklace;

    public NecklaceManager(String necklaceName) {
        this.necklace = new Necklace(necklaceName);
    }

    public void loadFromCsv(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Gem gem = GemFactory.fromCsv(line);
                    necklace.addGem(gem);
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping invalid line: " + line);
                }
            }
        }
    }

    public Necklace getNecklace() {
        return necklace;
    }

    public void printSummary() {
        System.out.println(necklace);
    }

    public List<Gem> findByTransparency(int min, int max) {
        return necklace.findByTransparencyRange(min, max);
    }

    public List<Gem> sortByTotalPriceDesc() {
        return necklace.sortByTotalPriceDesc();
    }
}
