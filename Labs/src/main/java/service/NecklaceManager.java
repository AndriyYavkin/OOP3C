package service;

import model.Gem;
import model.GemFactory;
import model.Necklace;

import java.io.*;
import java.util.*;

/**
 * Manages loading and operations on a Necklace.
 */
public class NecklaceManager {
    private final Map<String, Necklace> necklaces = new HashMap<>();
    private final String filePath;

    public NecklaceManager(String filePath) {
        this.filePath = filePath;
    }

    public void loadFromFile(GemManager gemManager) {
        File file = new File(filePath);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split(";");
                if (p.length < 6) continue;
                String name = p[0];
                String gemData = String.join(";", Arrays.copyOfRange(p, 1, 6));
                necklaces.putIfAbsent(name, new Necklace(name));
                necklaces.get(name).addGem(GemFactory.fromCsv(gemData));
            }
            System.out.println("Loaded " + necklaces.size() + " necklaces.");
        } catch (IOException e) {
            System.err.println("Error loading necklaces: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Necklace n : necklaces.values()) {
                for (Gem g : n.getGems()) {
                    writer.write(String.format("%s;%s;%s;%.2f;%.2f;%d%n",
                            n.getName(),
                            g.getClass().getSimpleName().contains("Precious") ? "Precious" : "SemiPrecious",
                            g.getName(), g.getWeightCarats(), g.getPricePerCarat(), g.getTransparency()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving necklaces: " + e.getMessage());
        }
    }

    public void createNecklaceInteractive(Scanner scanner) {
        System.out.print("Enter necklace name: ");
        String name = scanner.nextLine().trim();
        if (name.isBlank() || necklaces.containsKey(name)) {
            System.out.println("Invalid or duplicate name.");
            return;
        }
        necklaces.put(name, new Necklace(name));
        saveToFile();
        System.out.println("Necklace created.");
    }

    public void listAllNecklaces() {
        if (necklaces.isEmpty()) {
            System.out.println("No necklaces available.");
            return;
        }
        System.out.println("\n--- All Necklaces ---");
        necklaces.keySet().forEach(System.out::println);
    }

    public void showNecklaceDetailsInteractive(Scanner scanner) {
        Necklace necklace = selectNecklace(scanner);
        if (necklace != null)
            System.out.println(necklace);
    }

    public void addGemToNecklaceInteractive(Scanner scanner, GemManager gemManager) {
        Necklace necklace = selectNecklace(scanner);
        if (necklace == null) return;
        Gem gem = gemManager.selectGemInteractive(scanner);
        if (gem == null) return;
        necklace.addGem(gem);
        saveToFile();
        System.out.println("Gem added.");
    }

    public void removeGemFromNecklaceInteractive(Scanner scanner) {
        Necklace necklace = selectNecklace(scanner);
        if (necklace == null) return;
        List<Gem> gems = necklace.getGems();
        if (gems.isEmpty()) {
            System.out.println("No gems in this necklace.");
            return;
        }
        for (int i = 0; i < gems.size(); i++)
            System.out.printf("%d) %s%n", i + 1, gems.get(i));

        System.out.print("Select gem number to remove: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine());
            if (idx >= 1 && idx <= gems.size()) {
                Gem removed = gems.remove(idx - 1);
                saveToFile();
                System.out.println("Removed: " + removed);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    public void sortNecklaceInteractive(Scanner scanner) {
        Necklace necklace = selectNecklace(scanner);
        if (necklace == null) return;
        System.out.println("--- Sorted by total price (desc) ---");
        necklace.sortByTotalPriceDesc().forEach(System.out::println);
    }

    public void findByTransparencyInteractive(Scanner scanner) {
        Necklace necklace = selectNecklace(scanner);
        if (necklace == null) return;
        try {
            System.out.print("Min transparency (0-100): ");
            int min = Integer.parseInt(scanner.nextLine());
            System.out.print("Max transparency (0-100): ");
            int max = Integer.parseInt(scanner.nextLine());
            necklace.findByTransparencyRange(min, max).forEach(System.out::println);
        } catch (NumberFormatException e) {
            System.out.println("Invalid numbers.");
        }
    }

    public void removeNecklaceInteractive(Scanner scanner) {
        System.out.print("Enter necklace name: ");
        String name = scanner.nextLine().trim();
        if (necklaces.remove(name) != null) {
            saveToFile();
            System.out.println("Necklace removed.");
        } else {
            System.out.println("Not found.");
        }
    }

    private Necklace selectNecklace(Scanner scanner) {
        if (necklaces.isEmpty()) {
            System.out.println("No necklaces.");
            return null;
        }
        List<String> names = new ArrayList<>(necklaces.keySet());
        for (int i = 0; i < names.size(); i++)
            System.out.printf("%d) %s%n", i + 1, names.get(i));
        System.out.print("Select: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice >= 1 && choice <= names.size())
                return necklaces.get(names.get(choice - 1));
        } catch (NumberFormatException ignored) {}
        System.out.println("Invalid choice.");
        return null;
    }
}
