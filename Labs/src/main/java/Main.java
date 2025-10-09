import service.*;

public class Main {
    public static void main(String[] args) {
        NecklaceManager manager = new NecklaceManager("Luxury Gems");
        try {
            manager.loadFromCsv("data/gems.csv");
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
        }

        manager.printSummary();

        System.out.println("Sorted by price:");
        manager.sortByTotalPriceDesc().forEach(System.out::println);

        System.out.println("\nFiltered by transparency (85 - 95):");
        manager.findByTransparency(85, 95).forEach(System.out::println);

    }
}