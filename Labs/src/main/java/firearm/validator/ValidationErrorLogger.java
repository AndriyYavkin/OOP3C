package firearm.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ValidationErrorLogger implements ErrorHandler {

    private static final Logger logger = LogManager.getLogger(ValidationErrorLogger.class);
    private boolean isValid = true;

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        logger.warn("Validation Warning: Line {}, Column {}: {}", 
                    exception.getLineNumber(), exception.getColumnNumber(), exception.getMessage());
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        logger.error("Validation Error: Line {}, Column {}: {}", 
                     exception.getLineNumber(), exception.getColumnNumber(), exception.getMessage());
        isValid = false;
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        logger.fatal("Fatal Validation Error: Line {}, Column {}: {}", 
                     exception.getLineNumber(), exception.getColumnNumber(), exception.getMessage());
        isValid = false;
    }

    public boolean isValid() {
        return isValid;
    }
}