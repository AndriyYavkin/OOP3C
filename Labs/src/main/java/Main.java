import firearm.model.Firearms;
import firearm.parser.DomParser;
import firearm.parser.GunParser;
import firearm.parser.SaxParser;
import firearm.parser.StaxParser;
import firearm.validator.XsdValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final String XML_FILE_PATH = "src/main/resources/guns.xml";

    public static void main(String[] args) {
        
        logger.info("Application starting...");

        XsdValidator validator = new XsdValidator();
        boolean isValid = validator.validate(XML_FILE_PATH);
        
        logger.info("XML file is valid: {}", isValid);

        if (isValid) {
            try {
                logger.info("--- Parsing with DOM ---");
                GunParser domParser = new DomParser();
                Firearms firearmsDOM = domParser.parse(XML_FILE_PATH);
                logger.info("DOM Result: {}", firearmsDOM.toString());

                logger.info("--- Parsing with SAX ---");
                GunParser saxParser = new SaxParser();
                Firearms firearmsSAX = saxParser.parse(XML_FILE_PATH);
                logger.info("SAX Result: {}", firearmsSAX.toString());

                logger.info("--- Parsing with StAX ---");
                GunParser staxParser = new StaxParser();
                Firearms firearmsStAX = staxParser.parse(XML_FILE_PATH);
                logger.info("StAX Result: {}", firearmsStAX.toString());

            } catch (Exception e) {
                logger.error("Failed to parse XML: {}", e.getMessage(), e);
            }
        }

        logger.info("Application finished.");
    }
}