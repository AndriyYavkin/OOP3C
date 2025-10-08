import model.*;

public class Main {
    public static void main(String[] args) {
        Gem diamond = new PreciousStone("Diamond", 1.5, 95, 12000);
        Gem amethyst = new SemiPreciousStone("Amethyst", 3.2, 80, 500);
        Gem emerald = new PreciousStone("Emerald", 2.0, 90, 8000);
        Gem opal = new SemiPreciousStone("Opal", 2.5, 70, 900);

        Necklace necklace = new Necklace("Great necklace");
        necklace.addGem(diamond);
        necklace.addGem(emerald);
        necklace.addGem(amethyst);
        necklace.addGem(opal);

        System.out.println(necklace);
    }
}