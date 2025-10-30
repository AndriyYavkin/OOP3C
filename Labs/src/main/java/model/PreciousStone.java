package model;

/**
 * Represents a precious gem.
 */
public class PreciousStone extends Gem {

    private final String certificationID;

    public PreciousStone(String name, double weightCarats, int transparency, double pricePerCarat, String certificationID) {
        super(name, weightCarats, transparency, pricePerCarat);
        this.certificationID = (certificationID == null || certificationID.isBlank()) ? "N/A" : certificationID;
    }

    public String getCertificationID() {
        return certificationID;
    }

    @Override
    public String toString() {
        String baseStr = super.toString();
        return baseStr.substring(0, baseStr.length() - 1) + 
               String.format(", certID=%s]", certificationID);
    }
}