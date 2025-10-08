import model.*;

public class Main {
    public static void main(String[] args) {
        Gem diamond = new PreciousStone("Diamond", 1.5, 95, 12000);
        Gem amethyst = new SemiPreciousStone("Amethyst", 3.2, 80, 500);

        System.out.println(diamond);
        System.out.println(amethyst);
    }
}