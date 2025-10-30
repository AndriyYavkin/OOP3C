import firearm.model.Firearms;
import firearm.parser.DomParser;
import firearm.parser.GunParser;
import firearm.transformer.XsltTransformer;
import firearm.util.GunModelComparator;
import firearm.validator.XsdValidator;
import java.util.Collections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final String XML_FILE_PATH = "src/main/resources/guns.xml";
    private static final String XSL_FILE_PATH = "group_by_origin.xsl";
    private static final String XML_OUTPUT_PATH = "guns_grouped.xml";

    public static void main(String[] args) {
        
        logger.info("Application starting...");

        XsdValidator validator = new XsdValidator();
        boolean isValid = validator.validate(XML_FILE_PATH);
        
        logger.info("XML file is valid: {}", isValid);

        if (isValid) {
            try {
                logger.info("--- Parsing with DOM ---");
                GunParser domParser = new DomParser();
                Firearms firearms = domParser.parse(XML_FILE_PATH);
                logger.info("DOM Result: {}", firearms.toString());

                logger.info("--- Sorting Guns by Model ---");
                Collections.sort(firearms.getGuns(), new GunModelComparator());
                logger.info("Sorted Result: {}", firearms.toString());
                
                logger.info("--- Performing XSLT Transformation ---");
                XsltTransformer transformer = new XsltTransformer();
                transformer.transform(XML_FILE_PATH, XSL_FILE_PATH, XML_OUTPUT_PATH);

            } catch (Exception e) {
                logger.error("An error occurred during processing: {}", e.getMessage(), e);
            }
        }

        logger.info("Application finished.");
    }
}