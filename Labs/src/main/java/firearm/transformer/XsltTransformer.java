package firearm.transformer;

import java.io.File;
import java.net.URL;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class XsltTransformer {

    private static final Logger logger = LogManager.getLogger(XsltTransformer.class);

    public void transform(String xmlPath, String xslPath, String outputPath) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            
            URL xslUrl = getClass().getClassLoader().getResource(xslPath);
            if (xslUrl == null) {
                logger.error("Could not find XSL file: {}", xslPath);
                return;
            }
            
            StreamSource xslSource = new StreamSource(xslUrl.openStream());
            xslSource.setSystemId(xslUrl.toExternalForm());
            
            Transformer transformer = factory.newTransformer(xslSource);

            StreamSource xmlSource = new StreamSource(new File(xmlPath));
            StreamResult outputResult = new StreamResult(new File(outputPath));
            
            transformer.transform(xmlSource, outputResult);
            
            logger.info("Transformation successful. Output saved to: {}", outputPath);

        } catch (Exception e) {
            logger.error("XSLT Transformation failed: {}", e.getMessage(), e);
        }
    }
}