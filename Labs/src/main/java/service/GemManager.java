package service;

import model.Gem;
import model.GemFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Manages loading and operations on gems.
 */
public class GemManager {
    private final List<Gem> gems = new ArrayList<>();
    private final String filePath;

    public GemManager(String filePath) {
        this.filePath = filePath;
    }

    public void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    gems.add(GemFactory.fromCsv(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping invalid gem: " + line);
                }
            }
            System.out.println("Loaded " + gems.size() + " gems.");
        } catch (IOException e) {
            System.err.println("Error loading gems: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Gem g : gems) {
                writer.write(String.format("%s;%s;%.2f;%.2f;%d%n",
                        g.getClass().getSimpleName().contains("Precious") ? "Precious" : "SemiPrecious",
                        g.getName(), g.getWeightCarats(), g.getPricePerCarat(), g.getTransparency()));
            }
        } catch (IOException e) {
            System.err.println("Error saving gems: " + e.getMessage());
        }
    }

    public void showAllGems() {
        if (gems.isEmpty()) {
            System.out.println("No gems available.");
            return;
        }
        System.out.println("\n--- All Gems ---");
        for (int i = 0; i < gems.size(); i++)
            System.out.printf("%d) %s%n", i + 1, gems.get(i));
    }

    public void createGemInteractive(Scanner scanner) {
        try {
            System.out.print("Enter type (Precious/SemiPrecious): ");
            String type = scanner.nextLine().trim();
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Carats (>0): ");
            double carats = Double.parseDouble(scanner.nextLine());
            System.out.print("Price per carat (>0): ");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.print("Transparency (0-100): ");
            int transparency = Integer.parseInt(scanner.nextLine());

            if (carats <= 0 || price <= 0 || transparency < 0 || transparency > 100)
                throw new IllegalArgumentException("Invalid numeric values.");

            Gem gem = GemFactory.fromCsv(String.format("%s;%s;%.2f;%.2f;%d", type, name, carats, price, transparency));
            gems.add(gem);
            saveToFile();
            System.out.println("Created: " + gem);
        } catch (Exception e) {
            System.out.println("Error creating gem: " + e.getMessage());
        }
    }

    public Gem selectGemInteractive(Scanner scanner) {
        if (gems.isEmpty()) {
            System.out.println("No gems available.");
            return null;
        }
        showAllGems();
        System.out.print("Select gem number: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice >= 1 && choice <= gems.size())
                return gems.get(choice - 1);
        } catch (NumberFormatException ignored) {}
        System.out.println("Invalid selection.");
        return null;
    }

    public void removeGemInteractive(Scanner scanner) {
        showAllGems();
        if (gems.isEmpty()) return;
        System.out.print("Enter gem number to delete: ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            if (index < 1 || index > gems.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            Gem removed = gems.remove(index - 1);
            saveToFile();
            System.out.println("Removed gem: " + removed);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    public List<Gem> getGems() {
        return gems;
    }
}
