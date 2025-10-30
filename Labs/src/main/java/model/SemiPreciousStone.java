package model;

/**
 * Represents a semi-precious gem.
 */

public class SemiPreciousStone extends Gem {
    private final String originCountry;

    public SemiPreciousStone(String name, double weightCarats, int transparency, double pricePerCarat, String originCountry) {
        super(name, weightCarats, transparency, pricePerCarat);
        this.originCountry = (originCountry == null || originCountry.isBlank()) ? "Unknown" : originCountry;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    @Override
    public String toString() {
        String baseStr = super.toString();
        return baseStr.substring(0, baseStr.length() - 1) + 
               String.format(", origin=%s]", originCountry);
    }
}