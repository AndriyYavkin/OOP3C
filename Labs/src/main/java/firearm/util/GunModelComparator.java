package firearm.util;

import firearm.model.Gun;
import java.util.Comparator;

public class GunModelComparator implements Comparator<Gun> {

    @Override
    public int compare(Gun g1, Gun g2) {
        return g1.getModel().compareTo(g2.getModel());
    }
}