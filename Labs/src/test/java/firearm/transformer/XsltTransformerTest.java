package firearm.transformer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XsltTransformerTest {

    private static final String XSL_FILE_NAME = "group_by_origin.xsl";
    private String xmlPath;

    @BeforeEach
    void setUp() {
          URL resource = getClass().getClassLoader().getResource("guns.xml");
          assertNotNull(resource, "Test file guns.xml not found in resources.");
          xmlPath = resource.getPath();

          URL xslResource = getClass().getClassLoader().getResource(XSL_FILE_NAME);
          assertNotNull(xslResource, "XSL file " + XSL_FILE_NAME + " not found in resources.");
    }

    @Test
    void transform_ShouldProduceGroupedXml(@TempDir Path tempDir) throws IOException {
          Path outputFile = tempDir.resolve("transform_output.xml");

          XsltTransformer transformer = new XsltTransformer();
          transformer.transform(xmlPath, XSL_FILE_NAME, outputFile.toString());

          assertTrue(Files.exists(outputFile), "Transformed output file was not created.");

          String content = Files.readString(outputFile);

          assertTrue(content.contains("<Group origin=\"USA\">"), 
              "Output should contain group for USA.");
          assertTrue(content.contains("<Group origin=\"USSR\">"), 
              "Output should contain group for USSR.");
          assertTrue(content.contains("<Group origin=\"Belgium\">"), 
              "Output should contain group for Belgium.");

          assertTrue(content.matches("(?s).*<Group origin=\"USA\">.*<tns:Model>M16</tns:Model>.*</Group>.*"),
              "M16 should be in the USA group.");
          assertTrue(content.matches("(?s).*<Group origin=\"USSR\">.*<tns:Model>AK-47</tns:Model>.*</Group>.*"),
              "AK-47 should be in the USSR group.");
    }
}