package model;

public final class GemFactory {

    private GemFactory() {
    }

    public static Gem fromCsv(String csvLine) {
        if (csvLine == null || csvLine.isBlank()) {
            throw new IllegalArgumentException("CSV line cannot be null or empty");
        }

        String[] parts = csvLine.split(";");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid CSV format: " + csvLine);
        }

        String type = parts[0].trim();
        String name = parts[1].trim();
        double carats = Double.parseDouble(parts[2]);
        double price = Double.parseDouble(parts[3]);
        int transparency = Integer.parseInt(parts[4]);

        switch (type.toLowerCase()) {
            case "precious" -> {
                return new PreciousStone(name, carats, transparency, price);
            }
            case "semiprecious" -> {
                return new SemiPreciousStone(name, carats, transparency, price);
            }
            default -> throw new IllegalArgumentException("Unknown gem type: " + type);
        }
    }
}
