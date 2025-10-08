package model;

public abstract class Gem {
    private final String name;
    private final double weightCarats;
    private final int transparency; // 0–100 %
    private final double pricePerCarat;

    protected Gem(String name, double weightCarats, int transparency, double pricePerCarat) {
        this.name = name;
        this.weightCarats = weightCarats;
        this.transparency = transparency;
        this.pricePerCarat = pricePerCarat;
    }

    public String getName() { return name;}
     
    public double getWeightCarats() { return weightCarats; }

    public int getTransparency() { return transparency; }

    public double getPricePerCarat() { return pricePerCarat; }

    public double calculatePrice() { return weightCarats * pricePerCarat; }

    @Override
    public String toString() {
        return String.format("%s [%.2f ct, %d%%, %.2f$/ct, total=%.2f$]",
                name, weightCarats, transparency, pricePerCarat, calculatePrice());
    }
}
