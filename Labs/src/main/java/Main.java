

import firearm.model.FiringRange;
import firearm.model.Gun;
import firearm.model.Handy;
import firearm.model.TTC;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        
        logger.info("Application starting...");

        try {
            logger.debug("Creating a test Gun object.");
            
            Gun testGun = new Gun();
            testGun.setId("g001");
            testGun.setModel("AK-47");
            testGun.setHandy(Handy.TWO_HANDED);
            testGun.setOrigin("USSR");
            testGun.setMaterial("Wood/Steel");

            TTC standardTTC = new TTC();
            standardTTC.setFiringRange(FiringRange.MEDIUM);
            standardTTC.setAimRange(800);
            standardTTC.setMagazine(true);
            standardTTC.setOptics(false);
            
            testGun.setTtcs(new ArrayList<>());
            testGun.getTtcs().add(standardTTC);

            logger.debug("Gun object created successfully: {}", testGun.toString());
            
            logger.warn("This is a warning-level message. No real issue.");

        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage(), e);
        }

        logger.info("Application finished.");
    }
}