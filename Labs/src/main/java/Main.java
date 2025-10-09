import model.*;

public class Main {
    public static void main(String[] args) {
        Gem diamond = GemFactory.fromCsv("Precious;Diamond;1.5;12000;95");
        Gem amethyst = GemFactory.fromCsv("SemiPrecious;Amethyst;9.2;500;80");
        Gem emerald = GemFactory.fromCsv("Precious;Emerald;2.0;8000;90");
        Gem opal = GemFactory.fromCsv("SemiPrecious;Opal;2.5;900;70");

        Necklace necklace = new Necklace("Great necklace");
        necklace.addGem(diamond);
        necklace.addGem(emerald);
        necklace.addGem(amethyst);
        necklace.addGem(opal);

        System.out.println("=== Original Necklace ===");
        System.out.println(necklace);

        System.out.println("=== Sorted by total price (desc) ===");
        necklace.sortByTotalPriceDesc().forEach(System.out::println);

        System.out.println("\n=== Sorted by price per carat (desc) ===");
        necklace.sortByPricePerCaratDesc().forEach(System.out::println);
    }
}