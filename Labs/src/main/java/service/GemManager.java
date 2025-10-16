package service;

import model.Gem;
import model.GemFactory;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages loading and operations on gems.
 */
public class GemManager {
    private final List<Gem> gems = new ArrayList<>();

    public void loadFromDatabase() {
        gems.clear();
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT Type, Name, WeightCarats, PricePerCarat, Transparency FROM Gems")) {

            while (rs.next()) {
                gems.add(GemFactory.create(
                    rs.getString("Type"),
                    rs.getString("Name"),
                    rs.getDouble("WeightCarats"),
                    rs.getDouble("PricePerCarat"),
                    rs.getInt("Transparency")));
            }
            System.out.println("Loaded " + gems.size() + " gems from DB.");
        } catch (SQLException e) {
            System.err.println("Error loading gems: " + e.getMessage());
        }
    }

    public void saveGem(Gem g) {
        String type = g.getClass().getSimpleName().contains("Precious") ? "Precious" : "SemiPrecious";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO Gems (Type, Name, WeightCarats, PricePerCarat, Transparency) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, type);
            ps.setString(2, g.getName());
            ps.setDouble(3, g.getWeightCarats());
            ps.setDouble(4, g.getPricePerCarat());
            ps.setInt(5, g.getTransparency());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving gem: " + e.getMessage());
        }
    }

    public void showAllGems() {
        if (gems.isEmpty()) {
            System.out.println("No gems available.");
            return;
        }
        System.out.println("\n--- All Gems ---");
        int i = 1;
        for (Gem g : gems.stream().collect(Collectors.toList())) {
            System.out.printf("%d) %s%n", i++, g);
        }
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

            Gem gem = GemFactory.create(type, name, carats, price, transparency);
            gems.add(gem);
            saveGem(gem);
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
            try (Connection conn = Database.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM Gems WHERE Name = ?")) {
                ps.setString(1, removed.getName());
                ps.executeUpdate();
            }
            System.out.println("Removed gem: " + removed);
        } catch (NumberFormatException | SQLException e) {
            System.out.println("Error removing gem: " + e.getMessage());
        }
    }

    public List<Gem> getGems() {
        return new ArrayList<>(gems);
    }
}
