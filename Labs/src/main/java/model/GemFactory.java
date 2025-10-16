package model;

/**
 * Factory class for creating Gem instances from parameters.
 */
public final class GemFactory {

    private GemFactory() {
    }
    public static Gem create(String type, String name,
                             double weightCarats,
                             double pricePerCarat,
                             int transparency) {

        if (type == null || name == null) {
            throw new IllegalArgumentException("Type and name cannot be null.");
        }

        switch (type.trim().toLowerCase()) {
            case "precious" -> {
                return new PreciousStone(name, weightCarats, transparency, pricePerCarat);
            }
            case "semiprecious" -> {
                return new SemiPreciousStone(name, weightCarats, transparency, pricePerCarat);
            }
            default -> throw new IllegalArgumentException("Unknown gem type: " + type);
        }
    }
}