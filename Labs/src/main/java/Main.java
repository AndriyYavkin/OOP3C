import firearm.model.Firearms;
import firearm.model.FiringRange;
import firearm.model.Gun;
import firearm.model.Handy;
import firearm.model.TTC;
import firearm.parser.DomParser;
import firearm.parser.GunParser;
import firearm.parser.SaxParser;
import firearm.parser.StaxParser;
import firearm.transformer.XmlMarshaller;
import firearm.transformer.XsltTransformer;
import firearm.util.GunModelComparator;
import firearm.validator.XsdValidator;
import java.util.Collections;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    
    private static final String XML_FILE_PATH = "src/main/resources/guns.xml";
    private static final String XSL_FILE_PATH = "group_by_origin.xsl";
    
    private static final String XML_GROUPED_OUTPUT_PATH = "guns_grouped.xml";
    private static final String XML_MARSHALLED_OUTPUT_PATH = "guns_new_output.xml";

    public static void main(String[] args) {
        logger.info("Application starting...");

        // --- STEP 1: VALIDATION ---
        logger.info("--- STEP 1: Validating {} ---", XML_FILE_PATH);
        XsdValidator validator = new XsdValidator();
        if (!validator.validate(XML_FILE_PATH)) {
            logger.error("XML file is invalid. Halting execution.");
            return;
        }
        logger.info("Validation successful.");


        // --- STEP 2: PARSING (All three) ---
        Firearms firearmsDOM = runParser("DOM", new DomParser(), XML_FILE_PATH);
        runParser("SAX", new SaxParser(), XML_FILE_PATH);
        runParser("StAX", new StaxParser(), XML_FILE_PATH);
        
        if (firearmsDOM == null) {
            logger.error("Failed to parse with DOM. Halting execution.");
            return;
        }

        // --- STEP 3: SORTING ---
        logger.info("--- STEP 3: Sorting parsed objects ---");
        logger.debug("Unsorted list: {}", firearmsDOM.getGuns());
        Collections.sort(firearmsDOM.getGuns(), new GunModelComparator());
        logger.info("Sorted list by Model: {}", firearmsDOM.getGuns());


        // --- STEP 4: XSLT TRANSFORMATION ---
        logger.info("--- STEP 4: Performing XSLT transformation (Grouping) ---");
        XsltTransformer transformer = new XsltTransformer();
        transformer.transform(XML_FILE_PATH, XSL_FILE_PATH, XML_GROUPED_OUTPUT_PATH);
        logger.info("XSLT output saved to {}", XML_GROUPED_OUTPUT_PATH);


        // --- STEP 5: MODIFY & MARSHAL (Java -> XML) ---
        logger.info("--- STEP 5: Modifying data in Java and Marshalling to XML ---");
        
        Gun newGun = createBeretta();
        firearmsDOM.getGuns().add(newGun);
        logger.info("Added new object to list: {}", newGun.getModel());
        
        XmlMarshaller marshaller = new XmlMarshaller();
        marshaller.marshal(firearmsDOM, XML_MARSHALLED_OUTPUT_PATH);

        logger.info("Application finished successfully.");
    }

    private static Firearms runParser(String type, GunParser parser, String path) {
        logger.info("--- Parsing with {} ---", type);
        try {
            Firearms firearms = parser.parse(path);
            logger.info("Successfully parsed {} objects with {}", firearms.getGuns().size(), type);
            logger.debug("Result: {}", firearms);
            return firearms;
        } catch (Exception e) {
            logger.error("{} Parser failed: {}", type, e.getMessage(), e);
            return null;
        }
    }
    
    private static Gun createBeretta() {
        Gun gun = new Gun();
        gun.setId("g006");
        gun.setModel("Beretta 92");
        gun.setHandy(Handy.ONE_HANDED);
        gun.setOrigin("Italy");
        gun.setMaterial("Aluminum/Steel");
        
        TTC ttc = new TTC();
        ttc.setFiringRange(FiringRange.CLOSE);
        ttc.setAimRange(50);
        ttc.setMagazine(true);
        ttc.setOptics(false);
        
        gun.setTtcs(new ArrayList<>());
        gun.getTtcs().add(ttc);
        return gun;
    }
}