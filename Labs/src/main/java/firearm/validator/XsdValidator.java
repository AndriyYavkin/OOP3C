package firearm.validator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

public class XsdValidator {

    private static final Logger logger = LogManager.getLogger(XsdValidator.class);
    private static final String SCHEMA_FILE_NAME = "Gun.xsd";

    public boolean validate(String xmlFilePath) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            
            URL schemaUrl = getClass().getClassLoader().getResource(SCHEMA_FILE_NAME);
            if (schemaUrl == null) {
                logger.error("Could not find schema file: {}", SCHEMA_FILE_NAME);
                return false;
            }
            
            Schema schema = factory.newSchema(schemaUrl);
            Validator validator = schema.newValidator();
            
            ValidationErrorLogger errorHandler = new ValidationErrorLogger();
            validator.setErrorHandler(errorHandler);
            
            validator.validate(new StreamSource(new File(xmlFilePath)));
            
            return errorHandler.isValid();
            
        } catch (IOException | SAXException e) {
            logger.error("Validation exception: {}", e.getMessage(), e);
            return false;
        }
    }
}