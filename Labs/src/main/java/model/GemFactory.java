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
                             int transparency,
                             String certificationID,
                             String originCountry) {

        if (type == null || name == null) {
            throw new IllegalArgumentException("Type and name cannot be null.");
        }

        switch (type.trim().toLowerCase()) {
            case "precious" -> {
                return new PreciousStone(name, weightCarats, transparency, pricePerCarat, certificationID);
            }
            case "semiprecious" -> {
                return new SemiPreciousStone(name, weightCarats, transparency, pricePerCarat, originCountry);
            }
            default -> throw new IllegalArgumentException("Unknown gem type: " + type);
        }
    }
}