import service.GemManager;
import service.NecklaceManager;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final GemManager gemManager = new GemManager("gems.csv");
    private static final NecklaceManager necklaceManager = new NecklaceManager("necklaces.csv");

    public static void main(String[] args) {
        gemManager.loadFromFile();
        necklaceManager.loadFromFile(gemManager);

        boolean running = true;
        while (running) {
            showMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> gemManager.showAllGems();
                case "2" -> gemManager.createGemInteractive(scanner);
                case "3" -> necklaceManager.createNecklaceInteractive(scanner);
                case "4" -> necklaceManager.addGemToNecklaceInteractive(scanner, gemManager);
                case "5" -> necklaceManager.removeGemFromNecklaceInteractive(scanner);
                case "6" -> necklaceManager.listAllNecklaces();
                case "7" -> necklaceManager.showNecklaceDetailsInteractive(scanner);
                case "8" -> necklaceManager.sortNecklaceInteractive(scanner);
                case "9" -> necklaceManager.findByTransparencyInteractive(scanner);
                case "10" -> necklaceManager.removeNecklaceInteractive(scanner);
                case "11" -> gemManager.removeGemInteractive(scanner);
                case "0" -> {
                    gemManager.saveToFile();
                    necklaceManager.saveToFile();
                    running = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("""
        \n===  Necklace Manager ===
        1. Show all Gems
        2. Create new Gem
        3. Create new Necklace
        4. Add Gem to Necklace
        5. Remove Gem from Necklace
        6. List all Necklaces
        7. Show Necklace details
        8. Sort Gems in Necklace
        9. Find Gems by Transparency
        10. Remove Necklace
        11. Remove Gem from Gems List
        0. Exit
        """);
        System.out.print("Select option: ");
    }
}
