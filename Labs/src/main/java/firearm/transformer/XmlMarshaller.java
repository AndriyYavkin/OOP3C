package firearm.transformer;

import firearm.model.Firearms;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class XmlMarshaller {
    
    private static final Logger logger = LogManager.getLogger(XmlMarshaller.class);

    public void marshal(Firearms firearms, String outputPath) {
        try {
            JAXBContext context = JAXBContext.newInstance(Firearms.class);
            Marshaller marshaller = context.createMarshaller();
            
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.firearm.com/schema Gun.xsd");

            marshaller.marshal(firearms, new File(outputPath));
            
            logger.info("Successfully marshalled Java objects to {}", outputPath);
            
        } catch (Exception e) {
            logger.error("Failed to marshal Java objects to XML: {}", e.getMessage(), e);
        }
    }
}