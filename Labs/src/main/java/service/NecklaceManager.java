package service;

import model.Gem;
import model.GemFactory;
import model.Necklace;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages loading and operations on a Necklace.
 */
public class NecklaceManager {
    private final Map<String, Necklace> necklaces = new HashMap<>();

    public void loadFromDatabase(GemManager gemManager) {
        necklaces.clear();
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement()) {

            ResultSet rs = st.executeQuery("SELECT Id, Name FROM Necklaces");
            while (rs.next()) {
                necklaces.put(rs.getString("Name"), new Necklace(rs.getString("Name")));
            }

            ResultSet join = st.executeQuery("""
                SELECT n.Name AS NecklaceName, g.Type, g.Name AS GemName,
                       g.WeightCarats, g.PricePerCarat, g.Transparency
                FROM NecklaceGems ng
                JOIN Necklaces n ON ng.NecklaceId = n.Id
                JOIN Gems g ON ng.GemId = g.Id
            """);

            while (join.next()) {
                necklaces.get(join.getString("NecklaceName")).addGem(
                    GemFactory.create(
                        join.getString("Type"),
                        join.getString("GemName"),
                        join.getDouble("WeightCarats"),
                        join.getDouble("PricePerCarat"),
                        join.getInt("Transparency"))
);
            }

            System.out.println("Loaded " + necklaces.size() + " necklaces from DB.");

        } catch (SQLException e) {
            System.err.println("Error loading necklaces: " + e.getMessage());
        }
    }

    public void saveNecklace(Necklace n) {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT OR IGNORE INTO Necklaces (Name) VALUES (?)")) {
            ps.setString(1, n.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving necklace: " + e.getMessage());
        }
    }

    public void addGemToNecklace(Necklace n, Gem g) {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("""
                 INSERT INTO NecklaceGems (NecklaceId, GemId)
                 SELECT n.Id, g.Id FROM Necklaces n, Gems g
                 WHERE n.Name = ? AND g.Name = ?
             """)) {
            ps.setString(1, n.getName());
            ps.setString(2, g.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error linking gem to necklace: " + e.getMessage());
        }
    }

    public void createNecklaceInteractive(Scanner scanner) {
        System.out.print("Enter necklace name: ");
        String name = scanner.nextLine().trim();
        if (name.isBlank() || necklaces.containsKey(name)) {
            System.out.println("Invalid or duplicate name.");
            return;
        }
        Necklace n = new Necklace(name);
        necklaces.put(name, n);
        saveNecklace(n);
        System.out.println("Necklace created.");
    }

    public void listAllNecklaces() {
        if (necklaces.isEmpty()) {
            System.out.println("No necklaces available.");
            return;
        }
        System.out.println("\n--- All Necklaces ---");
        necklaces.keySet().stream().sorted().forEach(System.out::println);
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
        addGemToNecklace(necklace, gem);
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
                try (Connection conn = Database.getConnection();
                    PreparedStatement ps = conn.prepareStatement("""
                        DELETE FROM NecklaceGems
                        WHERE NecklaceId = (SELECT n.Id FROM Necklaces n WHERE n.Name = ? LIMIT 1)
                        AND GemId = (SELECT g.Id FROM Gems g WHERE g.Name = ? LIMIT 1)
                    """)) {
                    ps.setString(1, necklace.getName());
                    ps.setString(2, removed.getName());
                    int affected = ps.executeUpdate();
                    if (affected > 0)
                        System.out.println("Removed: " + removed);
                    else
                        System.out.println("Gem not found in this necklace (nothing deleted).");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            try (Connection conn = Database.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM Necklaces WHERE Name = ?")) {
                ps.setString(1, name);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error deleting from DB: " + e.getMessage());
            }
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
        List<String> names = necklaces.keySet().stream().sorted().collect(Collectors.toList());
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