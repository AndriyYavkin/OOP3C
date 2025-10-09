package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Necklace {
    private final List<Gem> gems = new ArrayList<>();
    private final String name;
    
    public Necklace(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void addGem(Gem gem) {
        if (gem != null) {
            gems.add(gem);
        }
    }

    public List<Gem> getGems() {
        return Collections.unmodifiableList(gems);
    }

    public double getTotalWeight() {
        return gems.stream()
                .mapToDouble(Gem::getWeightCarats)
                .sum();
    }

    public double getTotalPrice() {
        return gems.stream()
                .mapToDouble(Gem::calculatePrice)
                .sum();
    }

    public List<Gem> sortByTotalPriceDesc() {
        List<Gem> sorted = new ArrayList<>(gems);
        sorted.sort(Comparator.comparingDouble(Gem::calculatePrice).reversed());
        return sorted;
    }

    public List<Gem> sortByPricePerCaratDesc() {
        List<Gem> sorted = new ArrayList<>(gems);
        sorted.sort(Comparator.comparingDouble(Gem::getPricePerCarat).reversed());
        return sorted;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s:%n", getName()));
        gems.forEach(gem -> sb.append("  - ").append(gem).append("\n"));
        sb.append(String.format("Total weight: %.2f ct, total price: %.2f$%n",
                getTotalWeight(), getTotalPrice()));
        return sb.toString();
    }
}