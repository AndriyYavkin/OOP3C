package firearm.util;

import firearm.model.Gun;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GunModelComparatorTest {

    @Test
    void compare_ShouldSortByModelAscending() {
        Gun gun1 = new Gun();
        gun1.setModel("M16");

        Gun gun2 = new Gun();
        gun2.setModel("AK-47");
        
        Gun gun3 = new Gun();
        gun3.setModel("Glock 19");

        List<Gun> guns = Arrays.asList(gun1, gun2, gun3);
        
        guns.sort(new GunModelComparator());

        assertEquals("AK-47", guns.get(0).getModel());
        assertEquals("Glock 19", guns.get(1).getModel());
        assertEquals("M16", guns.get(2).getModel());
    }
}