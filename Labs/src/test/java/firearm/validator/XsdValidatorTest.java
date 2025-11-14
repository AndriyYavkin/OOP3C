package firearm.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XsdValidatorTest {

    private XsdValidator validator;
    private String validXmlPath;
    private String invalidXmlPath;

    @BeforeEach
    void setUp() {
        validator = new XsdValidator();
          
        URL validUrl = getClass().getClassLoader().getResource("guns.xml");
        URL invalidUrl = getClass().getClassLoader().getResource("guns_invalid.xml");

        assertNotNull(validUrl, "Test file guns.xml not found in resources.");
        assertNotNull(invalidUrl, "Test file guns_invalid.xml not found in resources.");

        validXmlPath = validUrl.getPath();
        invalidXmlPath = invalidUrl.getPath();
    }

    @Test
    void validate_WithValidXml_ShouldReturnTrue() {
        boolean isValid = validator.validate(validXmlPath);
        assertTrue(isValid, "Validator should return true for a valid XML file.");
    }

    @Test
    void validate_WithInvalidXml_ShouldReturnFalse() {
        boolean isValid = validator.validate(invalidXmlPath);
        assertFalse(isValid, "Validator should return false for an invalid XML file.");
    }
}